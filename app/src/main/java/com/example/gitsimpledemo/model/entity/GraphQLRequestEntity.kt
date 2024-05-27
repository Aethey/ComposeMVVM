package com.example.gitsimpledemo.model.entity

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: GraphQLRequestEntity
 */


data class GraphQLRequestEntity(
    val user: String,
    val endCursor: String? = "",
    var sort: String = "DESC",
    val query: String =
        """
    query {user(login: "$user") {
    repositories(first: 10,after: "$endCursor", orderBy: {field: CREATED_AT, direction: $sort}, isFork: false) {
      pageInfo {
        endCursor
        hasNextPage
      }
      edges {
        cursor
        node {
          name
          createdAt
          description
          primaryLanguage {
            name
          }
          stargazers {
            totalCount
          }
          url
        }
      }
    }
  }
}

""",
    val requestQueryBody: GraphQLRequestBody = GraphQLRequestBody(query = query)
)

data class GraphQLRequestBody(
    val query: String,
)


