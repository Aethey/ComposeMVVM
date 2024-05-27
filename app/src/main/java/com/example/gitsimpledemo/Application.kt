package com.example.gitsimpledemo

import android.app.Application
import androidx.room.Room
import com.example.gitsimpledemo.data.database.AppDatabase
import com.example.gitsimpledemo.data.database.MIGRATION_1_2
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.util.LanguageColorManager

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description: Application init db,Retrofit and some utils
 */
class Application : Application() {
    companion object {
        lateinit var instance: com.example.gitsimpledemo.Application
            private set
    }

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        // init retrofit
        RetrofitManager.initialize(this)
        //Instantiate a room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).addMigrations(MIGRATION_1_2).build()
        // init language color manager
        LanguageColorManager.initialize(this)
    }
}