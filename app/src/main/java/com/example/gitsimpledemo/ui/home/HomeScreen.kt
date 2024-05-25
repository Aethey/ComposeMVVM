package com.example.gitsimpledemo.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gitsimpledemo.route.Screens
import com.example.gitsimpledemo.ui.userdetail.UserDetailScreen
import com.example.gitsimpledemo.ui.userlist.UserListScreen

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:
 */

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screens.List.route) {
        composable(Screens.List.route) { UserListScreen(navController = navController) }
        composable(Screens.Detail.route) { UserDetailScreen() }
    }

}
