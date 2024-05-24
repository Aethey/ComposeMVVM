package com.example.gitsimpledemo

import android.app.Application
import androidx.room.Room
import com.example.gitsimpledemo.data.database.AppDatabase
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * Author: Ryu
 * Date: 2024/05/22
 * Description:
 */
class GitSimpleDemoApp: Application() {
    companion object {
        lateinit var instance: GitSimpleDemoApp
            private set
    }

    private lateinit var flutterEngine : FlutterEngine
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
//        Instantiate a room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

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