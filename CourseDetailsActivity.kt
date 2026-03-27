package com.example.myapplication.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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

class CourseDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Bỏ FirebaseprojectTheme, dùng MaterialTheme trực tiếp
            MaterialTheme {
                CourseListScreen()
            }
        }
    }
}

@Composable
fun CourseListScreen() {
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
                Toast.makeText(context, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Danh sách khóa học",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                                    val intent = android.content.Intent(context, UpdateCourse::class.java)
                                    intent.putExtra("course_id", course.id)
                                    intent.putExtra("course_name", course.name)
                                    intent.putExtra("course_duration", course.duration)
                                    intent.putExtra("course_description", course.description)
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
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Thời lượng: ${course.duration}",
                                    style = MaterialTheme.typography.bodyMedium
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
}