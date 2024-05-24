package com.example.gitsimpledemo.ui.userlist

import com.example.gitsimpledemo.util.Trie
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.UserEntity
import com.example.gitsimpledemo.model.entity.UserEntityList

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
data class UserListState(
    val items: List<String> = emptyList(),
    val userList: UserEntityList = emptyList(),
    val isRefreshing: Boolean = false,
    val currentPage: Int = 0,
    val since: Int = 0,
    val hasMore: Boolean = true,
    val isSearching: Boolean = false,
    val isScrolling: Boolean = false,
    val isShowTopItem: Boolean = true,
    val trie: Trie = Trie(),
    val searchHistory: List<SearchHistoryEntity> = emptyList(),
    val currentSearching: String = "",
)