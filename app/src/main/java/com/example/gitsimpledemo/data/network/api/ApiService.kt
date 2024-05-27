package com.example.gitsimpledemo.data.network.api

import com.example.gitsimpledemo.model.entity.GraphQLRequestBody
import com.example.gitsimpledemo.model.entity.RepoGraphQLResponseEntity
import com.example.gitsimpledemo.model.entity.ResponseListEntity
import com.example.gitsimpledemo.model.entity.UserDetailEntity
import com.example.gitsimpledemo.model.entity.UserEntityList
import com.example.gitsimpledemo.model.entity.UserEntitySearchList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description: ApiService
 */

interface ApiService {
    // list users
    @GET("users")
    suspend fun listUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Long
    ): UserEntityList

    // single user
    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): UserDetailEntity

    //    search users
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): UserEntitySearchList

    // get repositories list without fork
    @GET("search/repositories")
    suspend fun listRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("since") since: Long
    ): ResponseListEntity

    // getUserDetail(userinfo)
    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): UserDetailEntity

    // get user's repositories(GraphQL)
    @POST("graphql")
    suspend fun getRepoListGraphQL(@Body body: GraphQLRequestBody): RepoGraphQLResponseEntity

}

suspend fun <T> callApiService(apiCall: suspend () -> T): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            NetworkResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> NetworkResult.Error(IOException("Network Error"))
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    NetworkResult.Error(IOException("HTTP $code $errorResponse"))
                }

                else -> NetworkResult.Error(IOException("Unknown Error"))
            }
        }
    }
}

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}