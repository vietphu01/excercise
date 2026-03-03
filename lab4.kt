package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var bill by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var result by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {

        TextField(
            value = bill,
            onValueChange = { bill = it },
            label = { Text("Enter bill") }
        )

        TextField(
            value = discount,
            onValueChange = { discount = it },
            label = { Text("Enter Discount(%)")}
        )

        Button(
            onClick = {
                val billNum = bill.toFloatOrNull()
                val discountNum = discount.toFloatOrNull()
                if (billNum != null && billNum > 0) {
                    if (discountNum != null) {
                        if(discountNum > 0){
                            result = billNum- (billNum*(discountNum/100))
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Caculate bill")
        }

        Text(
            text = "Total Bill: $result",
            fontSize = 30.sp
        )
    }
}
