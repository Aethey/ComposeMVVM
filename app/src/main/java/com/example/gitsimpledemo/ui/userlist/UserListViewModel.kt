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
        loadInitialData()
    }

    private fun loadInitialData() {
        fetchData(uiState.since, clearList = true)
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
       if(query.isNotBlank()){
           viewModelScope.launch {
               val newHistory = SearchHistoryEntity(searchQuery = query.trim(), type = type)
               searchHistoryDao.upsert(newHistory)
               uiState.trie.insert(query.trim())
               updateSearchHistory()
           }
       }
    }

    fun searchQueryUpdate(query: String) {
        if (query.isEmpty()) {
            updateSearchHistory()
        } else {
            val results = uiState.trie.search(query.trim())
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
            uiState = uiState.copy(isSearching = searchState)
        }
    }

    fun updateUserListScrollState(scrollState: Boolean) {
        uiState = uiState.copy(isScrolling = scrollState)
    }

    fun onJudgmentShowTop(isShowTopItem: Boolean) {
        uiState = uiState.copy(isShowTopItem = isShowTopItem)
    }

    fun loadMoreData() {
        val query = uiState.currentSearching.ifEmpty { null }
        fetchData(uiState.since, query = query, append = true)
    }

    fun getSearchUserList(query: String) {
        fetchData(0, query = query, clearList = true)
    }

    fun refreshData() {
        uiState = uiState.copy(isRefreshing = true)
        fetchData(
            0,
            query = uiState.currentSearching.ifEmpty { null }
        )
    }

    private fun updateSearchHistory() {
        viewModelScope.launch {
            val history = searchHistoryDao.getAllHistorySortedByTime()
            println("history is $history")
            uiState = uiState.copy(searchHistory = history)
        }
    }

    private fun fetchData(
        since: Long,
        query: String? = null,
        append: Boolean = false,
        clearList: Boolean = false
    ) {
        viewModelScope.launch {
            val result = if (query.isNullOrBlank()) {
                repository.getData(since)
            } else {
                println("query is $query")
                repository.searchUsers(query.trim(), since)
            }
            when (result) {
                is NetworkResult.Success -> {
                    uiState = uiState.copy(
                        userList = if (append) uiState.userList + result.data else result.data,
                        currentPage = if (clearList) 1 else uiState.currentPage + 1,
                        hasMore = result.data.size == Constants.PAGE_SIZE,
                        since =if (result.data.isEmpty()) 1 else result.data.last().id,
                        isRefreshing = false,
                        isError = false
                    )
                }

                is NetworkResult.Error -> {
                    println("NetworkResult.Error error")
                    uiState = uiState.copy(
                        errorMessage = result.exception.message ?: "net error",
                        isRefreshing = false ,
                        isLoading = false,
                        hasMore= false,
                        isError = true
                    )
                }

                NetworkResult.Loading -> {
                    // Handle loading state if needed
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