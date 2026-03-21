package com.example.myapplication.lab6.mutiscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){

                    

                    ComposeNavigation()
                }
        }
    }
}


@Composable
fun ComposeNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "first_screen",  builder = {

            composable("first_screen") {
                FirstScreen(navController = navController)
            }
            composable("second_screen") {
                SecondScreen(navController = navController)
            }
            composable("third_screen") {
                ThirdScreen(navController = navController)
            }

            composable("Fourscreen/{data}", arguments = listOf(navArgument("data") { type = NavType.StringType })) { backStackEntry ->
                FourScreen(navController, backStackEntry.arguments?.getString("data") ?: "")
            }



        })
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavigationTheme {
        Greeting("Android")
    }
}

@Composable
fun NavigationTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
