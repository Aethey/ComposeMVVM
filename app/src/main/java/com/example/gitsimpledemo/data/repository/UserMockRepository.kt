package com.example.gitsimpledemo.data.repository

import com.example.gitsimpledemo.data.mock.MockData

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description: load local json(just REST API)
 */
class UserMockRepository(private val mockData: MockData) {
    private var currentPage = 0
    private val pageSize = 10

    fun getData(page: Int): List<String> {
        val startIndex = page * pageSize
        val endIndex = (page + 1) * pageSize
        return if (startIndex < mockData.data.size) {
            mockData.data.subList(startIndex, endIndex.coerceAtMost(mockData.data.size))
        } else {
            emptyList()
        }
    }

    fun getNextPage(): List<String> {
        val data = getData(currentPage)
        currentPage++
        return data
    }

    fun refreshData(): List<String> {
        currentPage = 0
        return getNextPage()
    }
}