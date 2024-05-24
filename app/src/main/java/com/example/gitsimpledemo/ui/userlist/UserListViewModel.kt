package com.example.gitsimpledemo.ui.userlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.data.repository.UserListRepository
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.SearchType
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

    var uiState by mutableStateOf(
        UserListState()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getData(uiState.since).let {
                uiState = uiState.copy(
                    userList = it,
                    currentPage = 1,
                    hasMore = it.size == 10,
                    since = it[it.size - 1].id.toInt()
                )
            }
        }
    }

    fun buildTrie() {
        viewModelScope.launch {
            val history = searchHistoryDao.getAllHistorySortedByTime()
            val trie = Trie().apply {
                history.forEach { insert(it.searchQuery) }
            }
            uiState = uiState.copy(trie = trie, searchHistory = history)
        }
    }

    fun addSearchQuery(query: String, type: SearchType) {
        viewModelScope.launch {
            val newHistory = SearchHistoryEntity(searchQuery = query, type = type)
            searchHistoryDao.insert(newHistory)
            uiState.trie.insert(query)
            val updatedHistory = searchHistoryDao.getAllHistorySortedByTime()
            uiState = uiState.copy(searchHistory = updatedHistory)
        }
    }

    fun searchQueryUpdate(query: String) {
        if (query.isEmpty()) {
            viewModelScope.launch {
                val history = searchHistoryDao.getAllHistorySortedByTime()
                uiState = uiState.copy(searchHistory = history)
            }
        } else {
            val results = uiState.trie.search(query)
            uiState = uiState.copy(searchHistory = results.map {
                SearchHistoryEntity(
                    searchQuery = it,
                    timestamp = 0L,
                    type = SearchType.USERNAME
                )
            })
        }
    }

    fun onUpdateSearchState(searchState: Boolean) {
        viewModelScope.launch {
            uiState = uiState.copy(
                isSearching = searchState
            )
        }
    }

    fun updateUserListScrollState(scrollState: Boolean) {
        uiState = uiState.copy(isScrolling = scrollState)
    }

    fun onJudgmentShowTop(isShowTopItem: Boolean) {
        uiState = uiState.copy(isShowTopItem = isShowTopItem)
    }

    fun loadMoreData() {
        viewModelScope.launch {
            if (uiState.currentSearching == "") {
                repository.getData(uiState.since).let {
                    uiState = uiState.copy(
                        userList = uiState.userList + it,
                        currentPage = uiState.currentPage + 1,
                        hasMore = it.size == 10,
                        since = it[it.size - 1].id.toInt()
                    )
                }

            } else {
                repository.searchUsers(uiState.currentSearching,uiState.since).let {
                    uiState = uiState.copy(
                        userList = uiState.userList + it,
                        currentPage = uiState.currentPage + 1,
                        hasMore = it.size == 10,
                        since = it[it.size - 1].id.toInt()
                    )
                }

            }

        }
    }

    fun getSearchUserList(query: String) {
        viewModelScope.launch {
            repository.searchUsers(query,0).let {
                uiState = uiState.copy(
                    currentPage = 0,
                    userList = it,
                    currentSearching = query,
                    since = it[it.size - 1].id.toInt()

                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            uiState = uiState.copy(isRefreshing = true)
            if (uiState.currentSearching == "") {
                repository.getData(0).let {
                    uiState = uiState.copy(
                        userList = it,
                        currentPage = 1,
                        hasMore = it.size == 10,
                        isRefreshing = false,
                        since = it[it.size - 1].id.toInt()
                    )
                }
            } else {
                repository.searchUsers(uiState.currentSearching,0).let {
                    uiState = uiState.copy(
                        userList = it,
                        currentPage = 1,
                        hasMore = it.size == 10,
                        isRefreshing = false,
                        since = it[it.size - 1].id.toInt()
                    )
                }
            }
        }
    }

}


class UserListViewModelFactory(private val searchHistoryDao: SearchHistoryDao) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserListViewModel(UserListRepository(RetrofitManager.createService(ApiService::class.java)), searchHistoryDao) as T
    }
}