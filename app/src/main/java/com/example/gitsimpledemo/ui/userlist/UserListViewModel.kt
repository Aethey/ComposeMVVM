package com.example.gitsimpledemo.ui.userlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.GitSimpleDemoApp
import com.example.gitsimpledemo.data.database.Trie
import com.example.gitsimpledemo.data.mock.MockData
import com.example.gitsimpledemo.data.repository.UserRepository
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.SearchType
import kotlinx.coroutines.launch

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
class UserListViewModel(private val repository: UserRepository,private val searchHistoryDao: SearchHistoryDao): ViewModel() {
    var uiState by mutableStateOf(
        UserListState()
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val initialData = repository.getData(0)
//            val history = searchHistoryDao.getAllHistorySortedByTime()

            uiState = uiState.copy(
                items = initialData,
                currentPage = 1,
                hasMore = initialData.size == 10,
//                searchHistory =history,
//                trie = Trie().apply {
//                    history.forEach { insert(it.searchQuery) }
//                }
            )

        }
    }

    fun buildTrie() {
        viewModelScope.launch {
            val history = searchHistoryDao.getAllHistorySortedByTime()
            val trie = Trie().apply {
                history.forEach { insert(it.searchQuery) }
            }
            uiState = uiState.copy(trie = trie,searchHistory =history)
        }
    }

    fun addSearchQuery(query: String,type: SearchType) {
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
            uiState = uiState.copy(searchHistory = results.map { SearchHistoryEntity(searchQuery = it, timestamp = 0L, type = SearchType.USERNAME) })
        }
    }

    fun onUpdateSearchState(searchState: Boolean){
        viewModelScope.launch {
            uiState = uiState.copy(
                isSearching = searchState
            )
        }
    }

    fun updateUserListScrollState(scrollState: Boolean){
        uiState = uiState.copy( isScrolling=scrollState)
    }

    fun onJudgmentShowTop(isShowTopItem:Boolean){
        uiState = uiState.copy( isShowTopItem = isShowTopItem )
    }

    fun loadMoreData() {
        viewModelScope.launch {
            val moreData = repository.getData(uiState.currentPage)
            uiState = uiState.copy(
                items = uiState.items + moreData,
                currentPage = uiState.currentPage + 1,
                hasMore = moreData.size == 10
            )
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            uiState = uiState.copy(isRefreshing = true)
            val refreshedData = repository.getData(0)
            uiState = uiState.copy(
                items = refreshedData,
                isRefreshing = false,
                currentPage = 1,
                hasMore = refreshedData.size == 10
            )
        }
    }
}


class UserListViewModelFactory(private val searchHistoryDao: SearchHistoryDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GitSimpleDemoApp
        return UserListViewModel(UserRepository(MockData()),searchHistoryDao) as T
    }
}