package com.example.gitsimpledemo.data.mock

import kotlin.random.Random

/**
 * Author: Ryu
 * Date: 2024/05/23
 * Description: test data for list
 */

class MockData {
    val data: List<String> = List(100) { "Item ${Random.nextInt(1000)}" }
    var tooLongString =
        "this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,this is a long string test,"
}