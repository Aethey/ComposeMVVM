package com.example.gitsimpledemo.ui.userlist

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
data class UserListState(
    val items: List<String> = emptyList(),
    val isRefreshing: Boolean = false,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)