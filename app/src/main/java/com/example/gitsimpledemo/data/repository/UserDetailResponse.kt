package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.callApiService
import com.example.gitsimpledemo.model.entity.GraphQLRequestEntity
import com.example.gitsimpledemo.model.entity.RepoGraphQLResponseOrganizationEntity
import com.example.gitsimpledemo.model.entity.RepoGraphQLResponseUserEntity
import com.example.gitsimpledemo.model.entity.UserDetailEntity

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: UserDetailResponse
 */
class UserDetailResponse(private val apiService: ApiService) {

    suspend fun getDataDetail(userName: String): NetworkResult<UserDetailEntity> {
        return callApiService {
            apiService.getUserDetail(username = userName)
        }
    }

    suspend fun getUserRepositoriesGraphQL(
        user: String,
        type: String,
        endCursor: String?
    ): NetworkResult<RepoGraphQLResponseUserEntity> {
        val requestQuery =
            GraphQLRequestEntity(user = user, type = type, endCursor = endCursor).requestQueryBody

        return callApiService {
            apiService.getUserRepoListGraphQL(requestQuery)
        }
    }

    suspend fun getOrganizationRepositoriesGraphQL(
        user: String,
        type: String,
        endCursor: String?
    ): NetworkResult<RepoGraphQLResponseOrganizationEntity> {
        val requestQuery =
            GraphQLRequestEntity(user = user, type = type, endCursor = endCursor).requestQueryBody

        return callApiService {
            apiService.getOrganizationRepoListGraphQL(requestQuery)
        }
    }
}