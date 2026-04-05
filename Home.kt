package com.example.myapplication.lab8

import Screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Chào mừng bạn đến với Trang Chủ!", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            // Khởi tạo Firebase Auth và gọi lệnh đăng xuất
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()

            // Chuyển hướng người dùng về lại màn hình Đăng nhập
            navController.navigate(Screen.SignIn.rout) {
                // Xóa lịch sử trang Home để người dùng không thể bấm nút Back (Trở về) trên điện thoại để quay lại Home được nữa
                popUpTo(Screen.Home.rout) { inclusive = true }
            }
        }) {
            Text("Đăng Xuất")
        }
    }
}