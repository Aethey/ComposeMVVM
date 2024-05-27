package com.example.gitsimpledemo.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 */


data class RepoGraphQLResponseEntity(
    @JsonProperty("data")
    val data: Data,
)

data class Data(
    @JsonProperty("user")
    val user: User,
)

data class User(
    @JsonProperty("repositories")
    val repositories: Repositories,
)

data class Repositories(
    @JsonProperty("pageInfo")
    val pageInfo: PageInfo,
    @JsonProperty("edges")
    val edges: List<RepoItem>,
)

data class PageInfo(
    @JsonProperty("endCursor")
    val endCursor: String,
    @JsonProperty("hasNextPage")
    val hasNextPage: Boolean,
)

data class RepoItem(
    @JsonProperty("cursor")
    val cursor: String,
    @JsonProperty("node")
    val node: Node,
)

data class Node(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("createdAt")
    val createdAt: String,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("primaryLanguage")
    val primaryLanguage: PrimaryLanguage?,
    @JsonProperty("stargazers")
    val stargazers: Stargazers,
    @JsonProperty("url")
    val url: String,
)

data class PrimaryLanguage(
    @JsonProperty("name")
    val name: String,
)

data class Stargazers(
    @JsonProperty("totalCount")
    val totalCount: Long,
)