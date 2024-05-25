package com.example.gitsimpledemo.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitsimpledemo.data.network.api.ApiService
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.data.repository.UserDetailResponse
import com.example.gitsimpledemo.data.repository.UserListRepository
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.ui.userlist.UserListViewModel

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description:
 */
class UserDetailViewModel (
    private val repository: UserDetailResponse
) : ViewModel() {

}

class UserDetailViewModelFactory() :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserDetailViewModel(
            UserDetailResponse(RetrofitManager.createService(ApiService::class.java)),
        ) as T
    }
}