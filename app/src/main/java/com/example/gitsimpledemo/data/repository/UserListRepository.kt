package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.Constants
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.callApiService
import com.example.gitsimpledemo.model.entity.UserEntity
import com.example.gitsimpledemo.model.entity.UserEntityList

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
class UserListRepository(private val apiService: ApiService) {
    private val pageSize = Constants.PAGE_SIZE

    suspend fun getData(since:Long): NetworkResult<UserEntityList> {
        return callApiService {
            apiService.listUsers(perPage = pageSize, since = since )
        }
    }

    suspend fun searchUsers(query: String,since:Long): NetworkResult<List<UserEntity>> {
        return callApiService {
            apiService.searchUsers(perPage = pageSize,query = query,since = since).items
        }
    }
}