package com.example.myapplication.lab8

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

data class Product(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val price: Double = 0.0,
    val imageUrl: String = ""
)

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF8EC5FC),
                    Color(0xFFE0C3FC)
                )
            ))
            .padding(16.dp)
        ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.SignIn.rout) {
                        popUpTo(Screen.Home.rout) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {

                Text("Đăng Xuất")
            }
        }
        ProductMainScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductMainScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val productList = remember { mutableStateListOf<Product>() }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    // Load danh sách sản phẩm
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

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dữ liệu sản phẩm", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên sản phẩm") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Loại sản phẩm") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(if (selectedImageUri == null) "Chọn Ảnh SP" else "Đã chọn ảnh mới")
            }

            // Hiển thị Preview ảnh khi chọn
            if (selectedImageUri != null) {
                AsyncImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.size(60.dp).padding(start = 16.dp), contentScale = ContentScale.Crop)
            } else if (editingProduct?.imageUrl?.isNotBlank() == true) {
                Base64Image(base64String = editingProduct!!.imageUrl, modifier = Modifier.size(60.dp).padding(start = 16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (name.isNotBlank() && type.isNotBlank() && price.isNotBlank()) {
                        isUploading = true
                        val productPrice = price.toDoubleOrNull() ?: 0.0

                        // Nén và chuyển ảnh thành chuỗi Base64
                        var base64Image = editingProduct?.imageUrl ?: ""
                        if (selectedImageUri != null) {
                            base64Image = encodeImageUriToBase64(context, selectedImageUri!!)
                        }

                        // Lưu thẳng vào Firestore (Bỏ qua Storage)
                        saveOrUpdateProduct(db, context, editingProduct, name, type, productPrice, base64Image) { success ->
                            isUploading = false
                            if (success) {
                                name = ""; type = ""; price = ""; selectedImageUri = null; editingProduct = null
                            }
                        }
                    } else {
                        Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                } else {
                    Text(if (editingProduct == null) "THÊM SẢN PHẨM" else "CẬP NHẬT SẢN PHẨM")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Text("Danh sách sản phẩm:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))

        // Hiển thị danh sách
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(productList) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                        // Hiển thị ảnh giải mã từ chuỗi Base64
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

                        Column {
                            IconButton(onClick = {
                                name = product.name; type = product.type; price = product.price.toString()
                                editingProduct = product; selectedImageUri = null
                            }) { Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = MaterialTheme.colorScheme.primary) }

                            IconButton(onClick = {
                                db.collection("Products").document(product.id).delete()
                                    .addOnSuccessListener { Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show() }
                            }) { Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }
        }
    }
}

// Hàm chuyển/nén ảnh thành văn bản
fun encodeImageUriToBase64(context: Context, uri: Uri): String {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream) // Nén ảnh xuống 20% chất lượng để lưu vừa Firestore
        val bytes = outputStream.toByteArray()
        Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (e: Exception) {
        ""
    }
}

@Composable
fun Base64Image(base64String: String, modifier: Modifier = Modifier) {
    // 1. Thực hiện giải mã ảnh bên trong try-catch và lưu kết quả vào biến bitmap
    val bitmap = try {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        null // Nếu lỗi thì gán bằng null
    }

    // 2. Gọi hàm giao diện (Image) ở bên NGOÀI khối try-catch
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Ảnh SP",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

fun saveOrUpdateProduct(db: FirebaseFirestore, context: Context, editingProduct: Product?, name: String, type: String, price: Double, imageUrl: String, onComplete: (Boolean) -> Unit) {
    val docRef = if (editingProduct != null) db.collection("Products").document(editingProduct.id) else db.collection("Products").document()
    val productToSave = Product(id = docRef.id, name = name, type = type, price = price, imageUrl = imageUrl)

    docRef.set(productToSave)
        .addOnSuccessListener {
            Toast.makeText(context, if (editingProduct != null) "Cập nhật thành công!" else "Thêm thành công!", Toast.LENGTH_SHORT).show()
            onComplete(true)
        }
        .addOnFailureListener {
            Toast.makeText(context, "Lỗi Firestore", Toast.LENGTH_SHORT).show()
            onComplete(false)
        }
}