package com.example.myapplication.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firebaseproject.Course
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }
}

@Composable
fun AppScreen() {
    var showAddForm by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 2 nút chuyển đổi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showAddForm = true }) {
                Text("Thêm khóa học")
            }
            Button(onClick = { showAddForm = false }) {
                Text("Danh sách")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị form thêm hoặc danh sách
        if (showAddForm) {
            AddCourseForm()
        } else {
            CourseList()
        }
    }
}

@Composable
fun AddCourseForm() {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tên khóa học") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Thời lượng") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && duration.isNotEmpty() && description.isNotEmpty()) {
                    val db = FirebaseFirestore.getInstance()
                    val course = hashMapOf(
                        "name" to name,
                        "duration" to duration,
                        "description" to description
                    )

                    db.collection("Courses")
                        .add(course)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                            name = ""
                            duration = ""
                            description = ""
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm khóa học")
        }
    }
}

@Composable
fun CourseList() {
    val context = LocalContext.current
    var courses by remember { mutableStateOf(listOf<Course>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load dữ liệu
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Courses")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Course>()
                for (doc in result) {
                    list.add(
                        Course(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            duration = doc.getString("duration") ?: "",
                            description = doc.getString("description") ?: ""
                        )
                    )
                }
                courses = list
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        courses.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có khóa học nào")
            }
        }
        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(courses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(context, UpdateCourse::class.java)
                                intent.putExtra("id", course.id)
                                intent.putExtra("name", course.name)
                                intent.putExtra("duration", course.duration)
                                intent.putExtra("description", course.description)
                                context.startActivity(intent)
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = course.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Thời lượng: ${course.duration}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = course.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}