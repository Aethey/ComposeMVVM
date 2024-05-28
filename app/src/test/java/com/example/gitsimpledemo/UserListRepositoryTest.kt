package com.example.gitsimpledemo

import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.repository.UserListRepository
import com.example.gitsimpledemo.model.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Author: Ryu
 * Date: 2024/05/28
 * Description:
 */
class UserListRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var userListRepository: UserListRepository


    @Before
    fun setUp() {
        apiService = mock()
        userListRepository = UserListRepository(apiService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test getData returns correct data`() = runTest {

        val mockUserList = listOf(UserEntity("login", 1L, "", ""), UserEntity("login1", 1L, "", ""))

        whenever(apiService.listUsers(AppConfig.PAGE_SIZE, 0)).thenReturn(mockUserList)
        val result = userListRepository.getData(0)
        assert(result is NetworkResult.Success && result.data[0].login == mockUserList[0].login)
        assert(result is NetworkResult.Success && result.data[1].login == "login1")
    }

}