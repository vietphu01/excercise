package com.example.myapplication // Đảm bảo dòng này khớp với dự án của bạn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Đảm bảo chỉ có MỘT class MainActivity ở đây
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                BusinessCardApp()
            }
        }
    }
}

@Composable
fun BusinessCardApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2E8D4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- PHẦN 1: Thông tin cá nhân ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            // Thay R.drawable.ic_launcher_foreground bằng ảnh logo của bạn nếu có
            val image = painterResource(id = R.drawable.anh)
            Image(
                painter = image,
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .background(Color(0xFF073042))
            )

            Text(
                text = "Nguyễn Phan Việt Phú",
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Android Developer",
                color = Color(0xFF3ddc84),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // --- PHẦN 2: Thông tin liên hệ ---
        Column(
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            ContactRow(Icons.Filled.Phone, "+84 765701720")
            ContactRow(Icons.Filled.Share, "@phu104")
            ContactRow(Icons.Filled.Email, "phunpv.24ai@vku.udn.vn")
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF3ddc84),
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(text = text, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BusinessCardApp()
}