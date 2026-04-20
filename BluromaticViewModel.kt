package com.example.myapplication.lab9

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

class BluromaticViewModel(private val bluromaticRepository: BluromaticRepository) : ViewModel() {

    // Lấy thông tin trạng thái của WorkManager (đang chạy, thành công, hay lỗi)
    val outputWorkInfo: Flow<WorkInfo?> = bluromaticRepository.outputWorkInfo

    // Hàm gọi khi người dùng bấm nút Start
    fun applyBlur(blurLevel: Int) {
        bluromaticRepository.applyBlur(blurLevel)
    }

    // Hàm gọi khi người dùng bấm nút Cancel
    fun cancelWork() {
        bluromaticRepository.cancelWork()
    }

    // Factory để khởi tạo ViewModel này vì nó có tham số truyền vào là Repository
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as android.app.Application).applicationContext
                BluromaticViewModel(bluromaticRepository = WorkManagerBluromaticRepository(context))
            }
        }
    }
}