package com.example.myapplication.lab9


import android.content.Context
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication.lab9.DELAY_TIME_MILLIS
import com.example.myapplication.lab9.KEY_BLUR_LEVEL
import com.example.myapplication.lab9.KEY_IMAGE_URI
import com.example.myapplication.R
import com.example.myapplication.lab9.OUTPUT_PATH
import com.example.myapplication.lab9.IMAGE_MANIPULATION_WORK_NAME
import com.example.myapplication.lab9.TAG_OUTPUT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    private var imageUri: Uri = context.getImageUri()
    private val workManager = WorkManager.getInstance(context)

    // Lắng nghe tiến trình của Work có gắn TAG_OUTPUT
    override val outputWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT).asFlow().mapNotNull {
            if (it.isNotEmpty()) it.first() else null
        }

    override fun applyBlur(blurLevel: Int) {
        // 1. Tạo chuỗi Work bắt đầu bằng việc dọn dẹp các file cũ
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // 2. Tạo WorkRequest để làm mờ ảnh
        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
        // Truyền dữ liệu vào (URI và BlurLevel)
        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))

        continuation = continuation.then(blurBuilder.build())

        // 3. Tạo WorkRequest để lưu ảnh, nhớ gắn Tag "TAG_OUTPUT"
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        // 4. Bắt đầu thực thi chuỗi
        continuation.enqueue()
    }

    override fun cancelWork() {
        // Hủy bỏ tác vụ nếu người dùng bấm nút Hủy
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, imageUri.toString()).putInt(KEY_BLUR_LEVEL, blurLevel)
        return builder.build()
    }
}