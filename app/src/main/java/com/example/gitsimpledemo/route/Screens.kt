package com.example.gitsimpledemo.route

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: init routes
 */
sealed class Screens(val route: String) {
    data object Login : Screens("login")
    data object Main : Screens("main")
    data object Home : Screens("home")
    data object List : Screens("list")
    data object Detail : Screens("detail/{username}") {
        fun createRoute(username: String) = "detail/$username"
    }
}