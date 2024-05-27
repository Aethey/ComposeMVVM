package com.example.gitsimpledemo.ui.userdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.data.repository.UserDetailResponse
import com.example.gitsimpledemo.model.dao.LanguageColorDao
import kotlinx.coroutines.launch

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description: UserDetailViewModel control user detail view state
 */
class UserDetailViewModel(
    private val repository: UserDetailResponse,
    private val languageColorDao: LanguageColorDao,
    private val username: String,
) : ViewModel() {
    var uiState by mutableStateOf(UserDetailState())

    init {
        onInitialData()
    }

    private fun onInitialData() {
        onGetUserDetail(username)
        onGetRepositories(username)
    }

    private fun onGetRepositories(
        userName: String,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getRepositoriesGraphQL(userName, uiState.endCursor).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        val hasMore = this.data.data.user.repositories.pageInfo.hasNextPage
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isError = false,
                            isRepositoriesLoading = false,
                            isLoadingMore = false,
                            listRepositories = if (append) uiState.listRepositories + this.data.data.user.repositories.edges else this.data.data.user.repositories.edges,
                            hasMore = hasMore,
                            endCursor = if (hasMore) {
                                this.data.data.user.repositories.pageInfo.endCursor
                            } else uiState.endCursor,
                            isEmpty = uiState.listRepositories.isEmpty() && this.data.data.user.repositories.edges.isEmpty(),
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isError = true,
                            isRepositoriesLoading = false,
                            isLoadingMore = false,
                            errorMessage = this.exception.message ?: "net error",
                            isEmpty = uiState.listRepositories.isEmpty(),
                        )
                    }

                    NetworkResult.Loading -> TODO()
                }
            }
        }
    }

    private fun onGetUserDetail(userName: String) {
        viewModelScope.launch {
            repository.getDataDetail(userName).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isError = false,
                            userID = this.data.id,
                            userName = this.data.login,
                            avatarUrl = this.data.avatarUrl,
                            followers = this.data.followers,
                            following = this.data.following,
                            name = this.data.name ?: uiState.name,
                            location = this.data.location ?: "No place info",
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isLoading = false,
                            isError = true,
                            errorMessage = this.exception.message ?: "net error",
                        )
                    }

                    NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    fun onRefreshData() {
        uiState = uiState.copy(isRefreshing = true, endCursor = "")
        onGetRepositories(username)
    }

    fun onLoadMoreData() {
        uiState = uiState.copy(isLoadingMore = true)
        onGetRepositories(uiState.userName, true)

    }
}


class UserDetailViewModelFactory(
    private val languageColorDao: LanguageColorDao,
    private val username: String,
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserDetailViewModel(
            UserDetailResponse(RetrofitManager.createService(ApiService::class.java)),
            languageColorDao,
            username
        ) as T
    }
}