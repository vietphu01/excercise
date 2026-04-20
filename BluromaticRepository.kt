package com.example.myapplication.lab9

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface BluromaticRepository {
    val outputWorkInfo: Flow<WorkInfo>
    fun applyBlur(blurLevel: Int)
    fun cancelWork()
}