package com.example.myapplication.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val duration = intent.getStringExtra("duration") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        setContent {
            UpdateScreen(id, name, duration, description)
        }
    }
}

@Composable
fun UpdateScreen(
    id: String,
    name: String,
    duration: String,
    description: String
) {
    val context = LocalContext.current
    var newName by remember { mutableStateOf(name) }
    var newDuration by remember { mutableStateOf(duration) }
    var newDescription by remember { mutableStateOf(description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cập nhật khóa học",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Tên khóa học") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newDuration,
            onValueChange = { newDuration = it },
            label = { Text("Thời lượng") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nút cập nhật
        Button(
            onClick = {
                if (newName.isNotEmpty() && newDuration.isNotEmpty() && newDescription.isNotEmpty()) {
                    val db = FirebaseFirestore.getInstance()
                    val updates = mapOf(
                        "name" to newName,
                        "duration" to newDuration,
                        "description" to newDescription
                    )

                    db.collection("Courses")
                        .document(id)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, MainActivity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cập nhật")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút xóa
        Button(
            onClick = {
                val db = FirebaseFirestore.getInstance()
                db.collection("Courses")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Xóa khóa học")
        }
    }
}