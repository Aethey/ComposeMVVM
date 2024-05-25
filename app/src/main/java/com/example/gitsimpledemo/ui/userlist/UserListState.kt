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
    /**
     * List of user data
     */
    val userList: UserEntityList = emptyList(),

    /**
     * Determine the state of page refresh
     */
    val isRefreshing: Boolean = false,

    /**
     * Determine the state of page data loading
     */
    val isLoading: Boolean = false,

    /**
     * Determine if there is more data to load
     */
    val hasMore: Boolean = true,

    /**
     * Determine if the search user page is open
     */
    val isSearching: Boolean = false,

    /**
     * Determine if the list is scrolling
     */
    val isScrolling: Boolean = false,

    /**
     * Determine if the list is scrolled to the top
     */
    val isShowTopItem: Boolean = true,

    /**
     * Determine if the network is error
     */
    val isError: Boolean = false,

    /**
     * Current page number of the list data
     */
    val currentPage: Int = 0,

    /**
     * Starting data ID for loading more data
     */
    val since: Long = 0,

    /**
     * Trie structure for search history records
     */
    val trie: Trie = Trie(),

    /**
     * List of search history records
     */
    val searchHistory: List<SearchHistoryEntity> = emptyList(),

    /**
     * Current search query
     */
    val searchQuery: String ="",

    /**
     * Error message
     */
    val errorMessage: String = "",

)