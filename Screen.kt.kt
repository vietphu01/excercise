package com.example.myapplication.lab8

sealed class Screen (val rout: String) {
    object SignIn : Screen("SignIn")
    object Home : Screen("Home")
    object UserScreen : Screen("UserScreen")
    object SignUp : Screen("SignUp")
}
