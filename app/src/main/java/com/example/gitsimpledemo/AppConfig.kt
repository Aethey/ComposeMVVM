package com.example.gitsimpledemo

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: Constants value
 */
object AppConfig {
    const val BASE_URL = "https://api.github.com/"
    const val CONNECT_TIMEOUT: Long = 30  //SECONDS
    const val READ_TIMEOUT: Long = 30  //SECONDS
    const val WRITE_TIMEOUT: Long = 30  //SECONDS
    const val PAGE_SIZE: Int = 10
    const val USE_MOCK: Boolean = false
    const val TYPE_USER: String = "User"
    const val TYPE_ORGANIZATION: String = "Organization"
    const val HEADER_AUTHORIZATION = "Authorization"
    private const val HEADER_AUTHORIZATION_BEARER = "Bearer "
    private const val HEADER_AUTHORIZATION_TOKEN = BuildConfig.API_KEY
    const val HEADER_TOKEN = HEADER_AUTHORIZATION_BEARER + HEADER_AUTHORIZATION_TOKEN
    const val USER_DETAIL_MOCK_NAME: String = "Aethey"
    const val NO_MORE: String = "You've reached the end."
    const val EMPTY_DATA: String = "sorry,data empty now."
    const val INIT_DATA: String = "Preparing data."
    const val EXIT_DIALOG_TITLE: String = "Exit this App."
    const val EXIT_DIALOG_CONTENT: String = "Are you sure you want to exit?"
    const val EXIT_DIALOG_SURE: String = "Confirm"
    const val EXIT_DIALOG_CANCEL: String = "Cancel"
    const val CLEAR_HISTORY_BUTTON: String = "clear history"
    const val TEXT_INPUT_HINT: String = "Search in Github"
}
