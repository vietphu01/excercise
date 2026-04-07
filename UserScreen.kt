package com.example.myapplication.lab8


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val productList = remember { mutableStateListOf<Product>() }

    // Load danh sách sản phẩm (giống hệt bên Admin)
    LaunchedEffect(Unit) {
        db.collection("Products").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                productList.clear()
                productList.addAll(list)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().fillMaxSize()
        .background(brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF8EC5FC),
                Color(0xFFE0C3FC)
            )
        ))
        .padding(16.dp)) {
        // Nút Đăng xuất
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("SignIn")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Đăng Xuất") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Danh sách sản phẩm (Dành cho User)", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Hiển thị danh sách NHƯNG KHÔNG CÓ NÚT SỬA/XÓA
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(productList) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                        // Hàm Base64Image lấy từ file trước
                        if (product.imageUrl.isNotBlank()) {
                            Base64Image(base64String = product.imageUrl, modifier = Modifier.size(60.dp))
                        } else {
                            AsyncImage(model = "https://via.placeholder.com/150", contentDescription = null, modifier = Modifier.size(60.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tên sp: ${product.name}", style = MaterialTheme.typography.bodyLarge)
                            Text("Giá sp: ${product.price}", style = MaterialTheme.typography.bodyMedium)
                            Text("Loại sp: ${product.type}", style = MaterialTheme.typography.bodySmall)
                        }
                        // XÓA PHẦN NÚT ICONBUTTON SỬA VÀ XÓA Ở ĐÂY
                    }
                }
            }
        }
    }
}