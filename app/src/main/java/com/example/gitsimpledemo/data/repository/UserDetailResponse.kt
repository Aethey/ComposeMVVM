package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.NetworkResult
import com.example.gitsimpledemo.data.network.api.callApiService
import com.example.gitsimpledemo.model.entity.GraphQLRequestEntity
import com.example.gitsimpledemo.model.entity.RepoGraphQLResponseEntity
import com.example.gitsimpledemo.model.entity.UserDetailEntity

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 */
class UserDetailResponse(private val apiService: ApiService) {

    suspend fun getDataDetail(userName: String): NetworkResult<UserDetailEntity> {
        print("getDataDetail")
        return callApiService {
            apiService.getUserDetail(username = userName)
        }
    }

    suspend fun getRepositoriesGraphQL(
        user: String,
        endCursor: String?
    ): NetworkResult<RepoGraphQLResponseEntity> {
        val requestQuery = GraphQLRequestEntity(user = user, endCursor = endCursor).requestQueryBody

        return callApiService {
            apiService.getRepoListGraphQL(requestQuery)
        }
    }

    //
    //call.enqueue(object : Callback<RepoGraphQLResponseEntity> {
    //    override fun onResponse(
    //        call: Call<RepoGraphQLResponseEntity>,
    //        response: Response<RepoGraphQLResponseEntity>
    //    ) {
    //        if (response.isSuccessful && response.body() != null) {
    //            val repositories = response.body()!!.data.user.repositories
    //            println("case is :${repositories.edges[0].node.name}")
    //        } else {
    //            // Handle error
    //            println("case is :${response.message()}")
    //        }
    //    }
    //
    //    override fun onFailure(call: Call<RepoGraphQLResponseEntity>, t: Throwable) {
    //        // Handle failure
    //    }
    //})


}