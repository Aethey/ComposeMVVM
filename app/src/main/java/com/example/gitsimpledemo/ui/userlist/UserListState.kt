package com.example.gitsimpledemo.ui.userlist

import androidx.compose.foundation.lazy.LazyListState

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
data class UserListState(
    val items: List<String> = emptyList(),
    val isRefreshing: Boolean = false,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val isSearching: Boolean = false,
    val isScrolling: Boolean = false,
    val isShowTopItem: Boolean = true,
)