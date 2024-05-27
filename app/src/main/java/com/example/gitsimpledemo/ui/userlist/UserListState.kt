package com.example.gitsimpledemo.ui.userlist

import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.UserEntityList
import com.example.gitsimpledemo.util.Trie

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
data class UserListState(
    // Contains the list of users. This list is displayed on the user list screen.
    val userList: UserEntityList = emptyList(),

    // Indicates whether the data in the list is currently being refreshed.
    val isRefreshing: Boolean = false,

    // Indicates whether data for the list is currently being loaded.
    val isLoading: Boolean = false,

    // Indicates whether more data is currently being loaded.
    val isLoadingMore: Boolean = false,

    // Indicates whether there are more items to load, used for implementing pagination.
    val hasMore: Boolean = false,

    // Indicates whether the search interface in the user list is open.
    val isSearching: Boolean = false,

    // Indicates if the list is currently being scrolled.
    val isScrolling: Boolean = false,

    // Indicates if the list is scrolled to the top item.
    val isShowTopItem: Boolean = true,

    // Indicates if there is a network error affecting data fetching.
    val isError: Boolean = false,

    // True if data is empty, otherwise false.
    val isEmpty: Boolean = false,

    // Current page number of the list data.
    val currentPage: Int = 0,

    // Starting data ID for loading more data, used in pagination.
    val since: Long = 0,

    // Trie structure for managing search history records.
    val trie: Trie = Trie(),

    // List of search history records.
    val searchHistory: List<SearchHistoryEntity> = emptyList(),

    // Current search query.
    val searchQuery: String = "",

    // Error message to be displayed in case of an error.
    val errorMessage: String = ""
)