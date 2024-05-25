package com.example.gitsimpledemo.ui.userlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.Constants
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.data.repository.UserListRepository
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.SearchType
import com.example.gitsimpledemo.model.entity.SearchViewType
import com.example.gitsimpledemo.util.Trie
import kotlinx.coroutines.launch

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
class UserListViewModel(
    private val repository: UserListRepository,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    var uiState by mutableStateOf(UserListState())

    init {
        onInitialData()
    }

    private fun onInitialData() {
        onGetDataList(0)
    }

    fun onBuildTrie() {
        viewModelScope.launch {
            val history = searchHistoryDao.getAllHistorySortedByTime()
            val trie = Trie().apply {
                history.forEach { insert(it.searchQuery) }
            }
            uiState = uiState.copy(trie = trie, searchHistory = history)
        }
    }

    fun onKeyboardSearch(type: SearchType) {
        println("type is onKeyboardSearch  ${uiState.searchQuery}")
        viewModelScope.launch {
            if (uiState.searchQuery.isNotBlank()) {
                val newHistory =
                    SearchHistoryEntity(searchQuery = uiState.searchQuery, type = type)
                searchHistoryDao.upsert(newHistory)
                uiState.trie.insert(uiState.searchQuery)
            }
            onGetSearchUserList(uiState.searchQuery)
        }
    }

    fun onClickSearch(type: SearchType, searchQuery: String) {
        println("type is onClickSearch  $searchQuery")
        viewModelScope.launch {
            val newHistory = SearchHistoryEntity(searchQuery = searchQuery, type = type)
//            searchQuery count  += 1 in history
            onGetSearchUserList(searchQuery)
        }

    }

    fun onRealtimeUpdateSearchQuery(query: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                // Realtime update searchQuery
                searchQuery = query,
            )
            uiState = uiState.copy(
                // Realtime update searchHistory
                searchHistory = if (query.isBlank()) {
                    searchHistoryDao.getAllHistorySortedByTime()
                } else searchHistoryDao.getAllHistoryContainingQuery(query)
            )
        }
//        why twice launch:
//       Effective state management for TextField in Compose
//       https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
    }

    fun onClearSearchHistory(){
        viewModelScope.launch {
            searchHistoryDao.clearSearchHistory()
            uiState = uiState.copy(searchHistory = emptyList())
        }
    }

    fun onUpdateSearchViewState(searchState: SearchViewType) {
        viewModelScope.launch {
            uiState = when (searchState) {
                SearchViewType.COMMON_CLOSE -> uiState.copy(isSearching = false)
                SearchViewType.CLEAR_CLOSE -> uiState.copy(isSearching = false, searchQuery = "")
                SearchViewType.SEARCH_CLOSE -> uiState.copy(isSearching = false)
                SearchViewType.OPEN -> uiState.copy(isSearching = true)
            }
        }
    }

    fun onUpdateUserListScrollState(scrollState: Boolean) {
        uiState = uiState.copy(isScrolling = scrollState)
    }

    fun onCheckListIsShowTop(isShowTopItem: Boolean) {
        uiState = uiState.copy(isShowTopItem = isShowTopItem)
    }

    fun onLoadMoreData() {
        if (uiState.searchQuery.isNotBlank()) {
            onGetSearchDataList(
                append = true,
                page = uiState.currentPage,
                query = uiState.searchQuery
            )
        } else {
            onGetDataList(
                since = uiState.since,
                append = true
            )
        }
    }

    private fun onGetSearchUserList(searchQuery: String) {
        if (searchQuery.isNotBlank()) {
            onGetSearchDataList(
                append = false,
                page = 1,
                query = searchQuery
            )
        } else {
            onGetDataList(
                since = 0,
                append = false
            )
        }
    }

    fun onRefreshData() {
        uiState = uiState.copy(isRefreshing = true)
        if (uiState.searchQuery.isNotBlank()) {
            onGetSearchDataList(
                append = false,
                page = 1,
                query = uiState.searchQuery
            )
        } else {
            onGetDataList(
                since = 0,
                append = false
            )
        }
    }


    private fun onGetDataList(
        since: Long,
        append: Boolean = false,
    ) {
        viewModelScope.launch {
            repository.getData(since).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        uiState = uiState.copy(
                            userList = if (append) uiState.userList + this.data else this.data,
                            hasMore = this.data.size == Constants.PAGE_SIZE,
                            since = if (this.data.isEmpty()) 0 else this.data.last().id,
                            isRefreshing = false,
                            isError = false,
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            errorMessage = this.exception.message ?: "net error",
                            isRefreshing = false,
                            isLoading = false,
                            hasMore = false,
                            isError = true
                        )

                    }

                    NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    private fun onGetSearchDataList(
        append: Boolean = false,
        page: Int,
        query: String,
    ) {
        viewModelScope.launch {
            repository.searchUsers(query, page).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        uiState = uiState.copy(
                            userList = if (append) uiState.userList + this.data else this.data,
                            currentPage = page + 1,
                            hasMore = this.data.size == Constants.PAGE_SIZE,
                            isRefreshing = false,
                            isError = false,
                            searchQuery = query
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            errorMessage = this.exception.message ?: "net error",
                            isRefreshing = false,
                            isLoading = false,
                            hasMore = false,
                            isError = true,
                            searchQuery = query
                        )
                    }

                    NetworkResult.Loading -> {
                    }
                }
            }
        }
    }
}

class UserListViewModelFactory(private val searchHistoryDao: SearchHistoryDao) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserListViewModel(
            UserListRepository(RetrofitManager.createService(ApiService::class.java)),
            searchHistoryDao
        ) as T
    }
}