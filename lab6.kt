package com.example.myapplication.lab6


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class FirstPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CupcakeApp()
        }
    }
}

@Composable
fun CupcakeApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {

        composable("start") {
            StartScreen(navController)
        }

        composable("flavor") {
            FlavorScreen(navController)
        }

        composable("pickup") {
            PickupScreen(navController)
        }

        composable("summary") {
            SummaryScreen(navController)
        }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Order Cupcakes", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("flavor")
        }) {
            Text("Order 1 Cupcake")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("flavor")
        }) {
            Text("Order 6 Cupcakes")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("flavor")
        }) {
            Text("Order 12 Cupcakes")
        }
    }
}

@Composable
fun FlavorScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Choose Flavor", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("pickup")
        }) {
            Text("Vanilla")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("pickup")
        }) {
            Text("Chocolate")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("pickup")
        }) {
            Text("Red Velvet")
        }
    }
}

@Composable
fun PickupScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Pickup Date", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("summary")
        }) {
            Text("Today")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("summary")
        }) {
            Text("Tomorrow")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("summary")
        }) {
            Text("Next Week")
        }
    }
}

@Composable
fun SummaryScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Order Summary", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Cupcakes Ordered")
        Text("Flavor Selected")
        Text("Pickup Date")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.popBackStack("start", false)
        }) {
            Text("Cancel Order")
        }
    }
}
