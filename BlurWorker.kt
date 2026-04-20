package com.example.myapplication.lab9

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        // Nhận dữ liệu URI và độ mờ từ Repository truyền sang
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        makeStatusNotification("Đang làm mờ ảnh...", applicationContext)

        return withContext(Dispatchers.IO) {
            // Giả lập delay một chút để nhìn rõ thông báo trên màn hình
            delay(DELAY_TIME_MILLIS)

            return@withContext try {
                require(!resourceUri.isNullOrBlank()) { "URI ảnh bị trống" }

                val resolver = applicationContext.contentResolver
                // Đọc ảnh từ URI
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                // Xử lý làm mờ (hàm gọi từ WorkerUtils)
                val output = blurBitmap(picture, blurLevel)

                // Ghi ra file tạm thời
                val outputUri = writeBitmapToFile(applicationContext, output)

                // Trả về URI ảnh mới để ghim vào outputWorkInfo
                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(TAG, "Lỗi khi áp dụng bộ lọc mờ", throwable)
                Result.failure()
            }
        }
    }
}