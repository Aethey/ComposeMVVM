package com.example.gitsimpledemo.model.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */
data class UserDetailEntity(
    val login: String,
    val id: Long,
    @JsonProperty("node_id")
    val nodeId: String,
    @JsonProperty("avatar_url")
    val avatarUrl: String,
    @JsonProperty("gravatar_id")
    val gravatarId: String,
    val url: String,
    @JsonProperty("html_url")
    val htmlUrl: String,
    @JsonProperty("followers_url")
    val followersUrl: String,
    @JsonProperty("following_url")
    val followingUrl: String,
    @JsonProperty("gists_url")
    val gistsUrl: String,
    @JsonProperty("starred_url")
    val starredUrl: String,
    @JsonProperty("subscriptions_url")
    val subscriptionsUrl: String,
    @JsonProperty("organizations_url")
    val organizationsUrl: String,
    @JsonProperty("repos_url")
    val reposUrl: String,
    @JsonProperty("events_url")
    val eventsUrl: String,
    @JsonProperty("received_events_url")
    val receivedEventsUrl: String,
    val type: String,
    @JsonProperty("site_admin")
    val siteAdmin: Boolean,
    val name: String,
    val company: Any?,
    val blog: String,
    val location: String,
    val email: String,
    val hireable: Any?,
    val bio: Any?,
    @JsonProperty("twitter_username")
    val twitterUsername: Any?,
    @JsonProperty("public_repos")
    val publicRepos: Long,
    @JsonProperty("public_gists")
    val publicGists: Long,
    val followers: Long,
    val following: Long,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("updated_at")
    val updatedAt: String,
    @JsonProperty("private_gists")
    val privateGists: Long,
    @JsonProperty("total_private_repos")
    val totalPrivateRepos: Long,
    @JsonProperty("owned_private_repos")
    val ownedPrivateRepos: Long,
    @JsonProperty("disk_usage")
    val diskUsage: Long,
    val collaborators: Long,
    @JsonProperty("two_factor_authentication")
    val twoFactorAuthentication: Boolean,
    val plan: Plan,
)


data class Plan(
    val name: String,
    val space: Long,
    val collaborators: Long,
    @JsonProperty("private_repos")
    val privateRepos: Long,
)
