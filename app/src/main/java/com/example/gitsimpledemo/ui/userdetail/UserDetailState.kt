package com.example.gitsimpledemo.ui.userdetail

import com.example.gitsimpledemo.model.entity.RepoItem

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description: State of the user detail screen in an application
 */
data class UserDetailState(
// The username of the user
    var userName: String = "",

    // The full name of the user.
    var name: String = "",

    // URL pointing to the user's avatar image.
    var avatarUrl: String = "",

    // Message to display when an error occurs.
    var errorMessage: String = "",

    // Cursor for implementing pagination in API requests. Points to the end of the current data page.
    var endCursor: String = "",

    // The user's geographical location.
    var location: String = "",

    // The current URL being displayed in the WebView.
    var currentUrl: String = "",

    // The number of followers the user has.
    var followers: Long = 0,

    // Unique identifier for the user, typically used in database and API interactions.
    var userID: Long = 0,

    // The number of other users this user is following.
    var following: Long = 0,

    // True if an error occurred during data fetching, otherwise false.
    val isError: Boolean = false,

    // True if data is currently being loaded, otherwise false.
    val isLoading: Boolean = false,

    // Indicates whether there are more items to load, used for implementing pagination.
    val hasMore: Boolean = false,

    // Indicates whether more data is currently being loaded.
    val isLoadingMore: Boolean = false,

    // True if the repositories are being loaded, otherwise false.
    val isRepositoriesLoading: Boolean = false,

    // Controls the visibility of a WebView component within the UI.
    val isShowWebView: Boolean = false,

    // Indicates whether the data (such as user details or repositories) is being refreshed.
    val isRefreshing: Boolean = false,

    // A list of repositories associated with the user, displayed in the UI.
    val listRepositories: List<RepoItem> = emptyList(),
)