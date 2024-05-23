package com.example.gitsimpledemo.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Icon
import com.example.gitsimpledemo.route.Screens

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:
 */

@Composable
fun BottomBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination


    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            modifier = Modifier.height(100.dp),
//            containerColor = Color.Gray,

        ) {

            BottomNavigationItem(
                icon = {
                    val isSelected = currentDestination?.route == Screens.List.route
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.5f else 1f,
                        animationSpec = tween(durationMillis = 300), label = "bottom"
                    )
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = null,
                        modifier = Modifier.scale(scale),
                        tint = if (isSelected) Color.Blue else Color.Gray

                    )
                },
                selected = currentDestination?.route == Screens.List.route,
                onClick = {
                    navController.navigate(Screens.List.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray

            )
            BottomNavigationItem(
                icon = {
                    val isSelected = currentDestination?.route == Screens.Detail.route
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.5f else 1f,
                        animationSpec = tween(durationMillis = 300), label = "bottom"
                    )
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.scale(scale),
                        tint = if (isSelected) Color.Blue else Color.Gray
                    )
                },
                selected = currentDestination?.route == Screens.Detail.route,
                onClick = {
                    navController.navigate(Screens.Detail.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            // 添加更多的 BottomNavigationItem
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}
