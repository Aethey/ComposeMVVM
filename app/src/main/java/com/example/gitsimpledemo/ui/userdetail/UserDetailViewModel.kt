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

    private fun onGetRepositories(userName: String) {
        viewModelScope.launch {
            repository.getRepositoriesGraphQL(userName, "").apply {
                when (this) {
                    is NetworkResult.Success -> {
                        val hasNextPage = this.data.data.user.repositories.pageInfo.hasNextPage
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isError = false,
                            isRepositoriesLoading = false,
                            listRepositories = this.data.data.user.repositories.edges,
                            endCursor = if (hasNextPage) {
                                this.data.data.user.repositories.pageInfo.endCursor
                            } else uiState.endCursor,
                            hasNextPage = hasNextPage,
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            isRefreshing = false,
                            isError = true,
                            isRepositoriesLoading = false,
                            errorMessage = this.exception.message ?: "net error",
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
                            name = this.data.name,
                            location = this.data.location,
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