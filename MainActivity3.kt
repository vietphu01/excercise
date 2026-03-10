package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity3 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WoofApp()
        }
    }
}

data class Dog(
    val name: String,
    val age: Int,
    val image: Int
)

val dogs = listOf(
    Dog("Koda", 2, R.drawable.anh),
    Dog("Lola", 16, R.drawable.anh),
    Dog("Frankie", 2, R.drawable.anh),
    Dog("Nox", 8, R.drawable.anh),
    Dog("Faye", 8, R.drawable.anh),
    Dog("Bella", 14, R.drawable.anh),
    Dog("Moana", 2, R.drawable.anh),
    Dog("Tzeitel", 7, R.drawable.anh),
    Dog("Leroy", 4, R.drawable.anh)
)

@Composable
fun WoofApp() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        items(dogs) { dog ->
            DogItem(dog)
        }
    }
}

@Composable
fun DogItem(dog: Dog) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDDE5DF)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(dog.image),
                contentDescription = dog.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = dog.name,
                    fontSize = 20.sp
                )

                Text(
                    text = "${dog.age} years old",
                    fontSize = 14.sp
                )
            }
        }
    }
}