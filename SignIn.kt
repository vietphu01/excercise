package com.example.myapplication.lab8

import Screen
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun SignIn(navController: NavController) {
    val context = LocalContext.current
    // Lưu trữ trạng thái của ô nhập liệu
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Khởi tạo công cụ xác thực của Firebase
    val firebaseAuth = FirebaseAuth.getInstance()

    // 1. Cấu hình yêu cầu lấy ID Token từ Google
    val token = "660968391786-srnr8hkes0ps0fqovufc20m6698oidt8.apps.googleusercontent.com"
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

// 2. Tạo bộ lắng nghe kết quả từ bảng chọn tài khoản
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            // Khi đã có thông tin Google, ta đưa mã đó cho Firebase để đăng nhập
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        navController.navigate(Screen.Home.rout)
                    } else {
                        Toast.makeText(context, "Lỗi Firebase: ${authTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(context, "Lỗi Google: ${e.statusCode} - ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Đăng Nhập", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Code sự kiện Click theo tài liệu Lab
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Đăng nhập thành công
                            navController.navigate(Screen.Home.rout)
                        } else {
                            // Đăng nhập thất bại, báo lỗi
                            Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Không được để trống thông tin!!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Đăng Nhập")
        }

        Button(onClick = {
            // Kích hoạt bảng chọn tài khoản Google hiện lên
            launcher.launch(googleSignInClient.signInIntent)
        }) {
            Text("Đăng nhập bằng Google")
        }

        TextButton(onClick = { navController.navigate(Screen.SignUp.rout) }) {
            Text("Chưa có tài khoản? Đăng ký ngay")
        }
    }
}