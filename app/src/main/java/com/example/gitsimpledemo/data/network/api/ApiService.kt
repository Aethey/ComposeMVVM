package com.example.gitsimpledemo.data.network.api

import com.example.gitsimpledemo.model.entity.ResponseListEntity
import com.example.gitsimpledemo.model.entity.UserDetailEntity
import com.example.gitsimpledemo.model.entity.UserEntity
import com.example.gitsimpledemo.model.entity.UserEntityList
import com.example.gitsimpledemo.model.entity.UserEntitySearchList
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

interface ApiService {
    // list users
    @GET("users")
    suspend fun listUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int
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
        @Query("since") since: Int
    ): UserEntitySearchList

    // get repositories list without fork
    @GET("search/repositories")
    suspend fun listRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("since") since: Int
    ): ResponseListEntity
}