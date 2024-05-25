package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.Constants
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.callApiService
import com.example.gitsimpledemo.model.entity.UserDetailEntity
import com.example.gitsimpledemo.model.entity.UserEntity

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 */
class UserDetailResponse(private val apiService: ApiService) {

    suspend fun getDataDetail(userName:String): NetworkResult<UserDetailEntity> {
        return callApiService {
            apiService.getUserDetail(username = userName)
        }
    }

}