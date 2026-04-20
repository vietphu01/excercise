package com.example.myapplication.lab9

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "CleanupWorker"

class CleanupWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        makeStatusNotification("Đang dọn dẹp các tệp cũ...", applicationContext)

        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS)
            return@withContext try {
                val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
                if (outputDirectory.exists()) {
                    val entries = outputDirectory.listFiles()
                    if (entries != null) {
                        for (entry in entries) {
                            val name = entry.name
                            if (name.isNotEmpty() && name.endsWith(".png")) {
                                val deleted = entry.delete()
                                Log.i(TAG, "Đã xóa $name - $deleted")
                            }
                        }
                    }
                }
                Result.success()
            } catch (exception: Exception) {
                Log.e(TAG, "Lỗi khi dọn dẹp tệp", exception)
                Result.failure()
            }
        }
    }
}