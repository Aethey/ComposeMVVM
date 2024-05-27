package com.example.gitsimpledemo.ui.userlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.AppConfig
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
 * Description: UserListViewModel control user list view state
 */
class UserListViewModel(
    private val repository: UserListRepository,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    var uiState by mutableStateOf(UserListState())

    init {
        onInitialData()
    }

    fun onInitialData() {
        uiState = uiState.copy(searchQuery = "")
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

    /*
    when keyboard search button clicked
    check search query is empty or not
    if not empty add search query to history then fetch data
    else fetch data with init (user null)
    then change search view state show list
     */
    fun onKeyboardSearch(type: SearchType) {
        uiState = uiState.copy(searchQuery = uiState.searchQueryInput)
        viewModelScope.launch {
            if (uiState.searchQueryInput.isNotBlank()) {
                val newHistory =
                    SearchHistoryEntity(searchQuery = uiState.searchQueryInput, type = type)
                searchHistoryDao.upsert(newHistory)
                //uiState.trie.insert(uiState.searchQueryInput)
            }
            onGetSearchUserList(uiState.searchQuery)
        }
    }

    /*
    click search mean click item in history list
    fetch data with item search query
    then change search view state show list
     */
    fun onClickSearch(type: SearchType, searchQuery: String) {
        viewModelScope.launch {
            // delete feature history count for sort
            //val newHistory = SearchHistoryEntity(searchQuery = searchQuery, type = type)
            //  searchQuery count  += 1 in history
            onGetSearchUserList(searchQuery)
        }

    }

    /*
    for realtime refresh search history list
    control keyboard input value in viewmodel(searchQueryInput)
    searchQueryInput is different from searchQuery
    when fetch request,add searchQueryInput to history and
    searchQuery = searchQueryInput
     */
    fun onRealtimeUpdateSearchQuery(query: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                //  Realtime update searchQuery
                searchQueryInput = query,
            )
            uiState = uiState.copy(
                //  Realtime update searchHistory
                searchHistory = if (query.isBlank()) {
                    searchHistoryDao.getAllHistorySortedByTime()
                } else searchHistoryDao.getAllHistoryContainingQuery(query)
            )
        }
        /**
         *  why twice launch:
         *  Effective state management for TextField in Compose
         *  https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
         */
    }

    // clear search history
    fun onClearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.clearSearchHistory()
            uiState = uiState.copy(searchHistory = emptyList(), searchQueryInput = "")
        }
    }


    /*
    control search view state
    COMMON_CLOSE is close search view when fetch request get data
    SEARCH_CLOSE is old feature
    CLEAR_CLOSE  is old feature
    OPEN is open search view

     */
    fun onUpdateSearchViewState(searchState: SearchViewType) {
        viewModelScope.launch {
            uiState = when (searchState) {
                SearchViewType.COMMON_CLOSE -> uiState.copy(isSearching = false)
                SearchViewType.CLEAR_CLOSE -> uiState.copy(
                    isSearching = false,
                    searchQuery = "",
                    searchQueryInput = ""
                )

                SearchViewType.SEARCH_CLOSE -> uiState.copy(isSearching = false)
                SearchViewType.OPEN -> uiState.copy(isSearching = true)
            }
        }
    }

    // get user listview scrollState, control FAB visible
    fun onUpdateUserListScrollState(scrollState: Boolean) {
        uiState = uiState.copy(isScrolling = scrollState)
    }

    // check user listview is show top item,control FAB style
    fun onCheckListIsShowTop(isShowTopItem: Boolean) {
        uiState = uiState.copy(isShowTopItem = isShowTopItem)
    }

    fun onLoadMoreData() {
        uiState = uiState.copy(isLoadingMore = true)
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

    // get userList with name
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

    // get user list without name,use since control loadMore state and paging
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
                            hasMore = this.data.size == AppConfig.PAGE_SIZE,
                            since = if (this.data.isEmpty()) 0 else this.data.last().id,
                            isRefreshing = false,
                            isError = false,
                            isLoadingMore = false,
                            isEmpty = uiState.userList.isEmpty() && this.data.isEmpty(),
                            searchQuery = ""
                        )
                    }

                    is NetworkResult.Error -> {
                        uiState = uiState.copy(
                            errorMessage = this.exception.message ?: "net error",
                            isRefreshing = false,
                            isLoading = false,
                            hasMore = false,
                            isError = true,
                            isLoadingMore = false
                        )
                    }

                    NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    // get user list with name,use page control loadMore state and paging
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
                            hasMore = this.data.size == AppConfig.PAGE_SIZE,
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