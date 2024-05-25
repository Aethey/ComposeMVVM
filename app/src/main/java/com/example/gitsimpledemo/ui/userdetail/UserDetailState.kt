package com.example.gitsimpledemo.ui.userdetail

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description:
 */
data class UserDetailState(
    var userName:String,
    var headerUrl:String,
    var followers:Long,
    var userID:Long,
    var following:Long,
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val isShowWebView: Boolean = false,

)