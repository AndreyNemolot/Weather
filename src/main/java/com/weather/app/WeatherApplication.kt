package com.weather.app

import android.app.Application
import androidx.room.Room
import com.weather.app.data.db.AppDatabase

class WeatherApplication: Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather_database"
        ).build()
    }
}