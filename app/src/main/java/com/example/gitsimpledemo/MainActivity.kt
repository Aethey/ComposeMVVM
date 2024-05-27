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

        composable(Screens.List.route) { UserListScreen(navController = navController) }
        composable(
            route = Screens.Detail.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("usertype") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val usertype = backStackEntry.arguments?.getString("usertype") ?: ""
            UserDetailScreen(
                navController = navController,
                username = username,
                usertype = usertype
            )
        }
    }
}

