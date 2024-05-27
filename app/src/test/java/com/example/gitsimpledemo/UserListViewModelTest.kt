package com.example.gitsimpledemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gitsimpledemo.data.repository.UserListRepository
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity
import com.example.gitsimpledemo.model.entity.SearchType
import com.example.gitsimpledemo.ui.userlist.UserListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


/**
 * Author: Ryu
 * Date: 2024/05/27
 * Description:
 */
@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: UserListRepository

    @Mock
    private lateinit var dao: SearchHistoryDao

    private lateinit var viewModel: UserListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = UserListViewModel(repository, dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateSearchQueryTest() = runTest {

        val query = "agentK"
        val mockSearchHistory = listOf(
            SearchHistoryEntity(
                type = SearchType.USERNAME,
                searchQuery = "agentK",
                timestamp = 123456L
            ),
            SearchHistoryEntity(
                type = SearchType.USERNAME,
                searchQuery = "agentK",
                timestamp = 123457L
            )
        )

        // Mock the DAO methods
        `when`(dao.getAllHistorySortedByTime()).thenReturn(emptyList())
        `when`(dao.getAllHistoryContainingQuery(query)).thenReturn(mockSearchHistory)

        // Set some values
        viewModel.onRealtimeUpdateSearchQuery("agentK")
        assertEquals("agentK", viewModel.uiState.searchQuery)
    }
}