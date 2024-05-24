package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.Constants
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.model.entity.UserEntity
import com.example.gitsimpledemo.model.entity.UserEntityList
import com.example.gitsimpledemo.model.entity.UserEntitySearchList
import retrofit2.HttpException

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description:
 */
class UserListRepository(private val apiService: ApiService) {
    private val pageSize = Constants.PAGE_SIZE

    suspend fun getData(since:Int): UserEntityList {
        return try {
            apiService.listUsers(perPage = pageSize, since = since )
        } catch (e: HttpException) {
            emptyList()
        }
    }

    suspend fun searchUsers(query: String,since:Int): UserEntityList {
        return try {
            apiService.searchUsers(perPage = pageSize,query = query,since = since).items
        }catch (e: HttpException) {
            emptyList()
        }
    }
}