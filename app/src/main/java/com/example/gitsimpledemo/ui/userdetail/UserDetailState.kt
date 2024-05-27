package com.example.gitsimpledemo.ui.userdetail

import com.example.gitsimpledemo.model.entity.RepoItem

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description:
 */
data class UserDetailState(
    var userName: String = "",
    var name: String = "",
    var avatarUrl: String = "",
    var errorMessage: String = "",
    var endCursor: String = "",
    var location: String = "",
    var currentUrl: String = "",
    var hasNextPage: Boolean = true,
    var followers: Long = 0,
    var userID: Long = 0,
    var following: Long = 0,
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val isRepositoriesLoading: Boolean = false,
    val isShowWebView: Boolean = false,
    val isRefreshing: Boolean = false,
    val listRepositories: List<RepoItem> = emptyList(),

    )