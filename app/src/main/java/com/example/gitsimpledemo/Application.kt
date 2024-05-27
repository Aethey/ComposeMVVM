package com.example.gitsimpledemo

import android.app.Application
import androidx.room.Room
import com.example.gitsimpledemo.data.database.AppDatabase
import com.example.gitsimpledemo.data.database.MIGRATION_1_2
import com.example.gitsimpledemo.data.network.api.RetrofitManager
import com.example.gitsimpledemo.util.LanguageColorManager
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:
 */
class Application : Application() {
    companion object {
        lateinit var instance: com.example.gitsimpledemo.Application
            private set
    }

    private lateinit var flutterEngine: FlutterEngine
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
//        Instantiate a room database
        RetrofitManager.initialize(this)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).addMigrations(MIGRATION_1_2).build()
        LanguageColorManager.initialize(this)

        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("flutter_engine_id", flutterEngine)
    }
}