package com.example.gitsimpledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitsimpledemo.route.Screens
import com.example.gitsimpledemo.ui.theme.GitSimpleDemoTheme
import com.example.gitsimpledemo.ui.userdetail.UserDetailScreen
import com.example.gitsimpledemo.ui.userlist.UserListScreen

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: MainActivity, init navController
 */
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
        //NavHost(navController = navController, startDestination = Screens.Detail.route) {
//        composable(Screens.Home.route) { HomeScreen() }
        composable(Screens.List.route) { UserListScreen(navController = navController) }
        //composable(Screens.Detail.route) { UserDetailScreen(navController = navController) }
        composable(
            route = Screens.Detail.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            if (username != null) {
                UserDetailScreen(navController = navController, username = username)
            }
        }
    }
}

