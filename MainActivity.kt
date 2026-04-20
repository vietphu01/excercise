package com.example.myapplication.lab9

// Nhớ import file R của bạn vào đây (ví dụ: import com.example.myapplication.R)
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo
import com.example.myapplication.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                val viewModel: BluromaticViewModel = viewModel(factory = BluromaticViewModel.Factory)
                val workInfo by viewModel.outputWorkInfo.collectAsState(initial = null)

                BluromaticScreen(
                    workInfo = workInfo,
                    onStartClick = { blurLevel -> viewModel.applyBlur(blurLevel) },
                    onCancelClick = { viewModel.cancelWork() }
                )
            }
        }
    }
}

@Composable
fun BluromaticScreen(
    workInfo: WorkInfo?,
    onStartClick: (Int) -> Unit,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current

    // Trích xuất URI ảnh kết quả từ WorkManager khi chạy thành công
    val outputUriString = workInfo?.outputData?.getString(KEY_IMAGE_URI)
    val isWorkFinished = workInfo?.state == WorkInfo.State.SUCCEEDED
    val isWorkRunning = workInfo?.state == WorkInfo.State.RUNNING

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Blur-O-Matic", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // KHU VỰC 1: HIỂN THỊ HÌNH ẢNH
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isWorkFinished && outputUriString != null) {
                // Nếu đã làm mờ xong, hiển thị ảnh từ file kết quả (URI)
                val bitmap = remember(outputUriString) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(Uri.parse(outputUriString))
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    } catch (e: Exception) { null }
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Ảnh đã làm mờ",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                // Nếu chưa làm hoặc đang làm, hiển thị ảnh gốc
                // ĐỔI TÊN "test_image" THÀNH TÊN ẢNH CỦA BẠN:
                Image(
                    painter = painterResource(id = R.drawable.anh),
                    contentDescription = "Ảnh gốc",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Hiển thị vòng xoay loading nếu WorkManager đang chạy ngầm
            if (isWorkRunning) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // KHU VỰC 2: CÁC NÚT CHỨC NĂNG
        Text(text = "Chọn mức độ làm mờ:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onStartClick(1) },
                enabled = !isWorkRunning
            ) {
                Text("Mờ Ít (1x)")
            }

            Button(
                onClick = { onStartClick(2) },
                enabled = !isWorkRunning
            ) {
                Text("Mờ Vừa (2x)")
            }

            Button(
                onClick = { onStartClick(3) },
                enabled = !isWorkRunning
            ) {
                Text("Mờ Nhiều (3x)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút Hủy (Chỉ hiện khi hệ thống đang chạy)
        if (isWorkRunning) {
            FilledTonalButton(
                onClick = onCancelClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Dừng quá trình")
            }
        }
    }
}