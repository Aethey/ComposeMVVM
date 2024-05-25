package com.example.gitsimpledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gitsimpledemo.route.Screens
import com.example.gitsimpledemo.ui.home.HomeScreen
import com.example.gitsimpledemo.ui.theme.GitSimpleDemoTheme
import com.example.gitsimpledemo.ui.userdetail.UserDetailScreen
import com.example.gitsimpledemo.ui.userlist.UserListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitSimpleDemoTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.List.route) {
//        composable(Screens.Home.route) { HomeScreen() }
        composable(Screens.List.route) { UserListScreen(navController = navController) }
        composable(Screens.Detail.route) { UserDetailScreen() }
    }
}

