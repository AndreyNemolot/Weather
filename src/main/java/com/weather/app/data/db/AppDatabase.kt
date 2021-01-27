package com.weather.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.app.data.db.entity.CityDao
import com.weather.app.data.db.entity.CityEntity

@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}