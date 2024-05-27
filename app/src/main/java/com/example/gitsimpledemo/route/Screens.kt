package com.example.gitsimpledemo.route

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: init routes
 */
sealed class Screens(val route: String) {
    data object List : Screens("list")
    data object Detail : Screens("detail/{username}/{usertype}") {
        fun createRoute(
            username: String,
            usertype: String,
        ) = "detail/$username/$usertype"
    }
}