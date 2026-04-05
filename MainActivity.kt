package com.example.myapplication.lab8

import Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavigation()
        }
    }
}


@Composable
fun MyNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.rout
    ) {
        composable(Screen.SignIn.rout) {
            SignIn(navController = navController)
        }
        composable(Screen.Home.rout) {
            HomeScreen(navController = navController)
        }
        composable(Screen.SignUp.rout) {
            SignUp(navController = navController)
        }
    }
}

