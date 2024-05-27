package com.example.gitsimpledemo.model.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description: UserEntity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserEntity(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("avatar_url")
    val avatarUrl: String,
//    no use in current feature
//    @JsonProperty("node_id")
//    val nodeId: String,
//    @JsonProperty("gravatar_id")
//    val gravatarId: String,
//    @JsonProperty("url")
//    val url: String,
//    @JsonProperty("html_url")
//    val htmlUrl: String,
//    @JsonProperty("followers_url")
//    val followersUrl: String,
//    @JsonProperty("following_url")
//    val followingUrl: String,
//    @JsonProperty("gists_url")
//    val gistsUrl: String,
//    @JsonProperty("starred_url")
//    val starredUrl: String,
//    @JsonProperty("subscriptions_url")
//    val subscriptionsUrl: String,
//    @JsonProperty("organizations_url")
//    val organizationsUrl: String,
//    @JsonProperty("repos_url")
//    val reposUrl: String,
//    @JsonProperty("events_url")
//    val eventsUrl: String,
//    @JsonProperty("received_events_url")
//    val receivedEventsUrl: String,
//    @JsonProperty("type")
//    val type: String,
//    @JsonProperty("site_admin")
//    val siteAdmin: Boolean,
)

typealias UserEntityList = List<UserEntity>;

data class UserEntitySearchList(
    @JsonProperty("total_count")
    val totalCount: Long,
    @JsonProperty("incomplete_results")
    val incompleteResults: Boolean,
    @JsonProperty("items")
    val items: List<UserEntity>,
)
