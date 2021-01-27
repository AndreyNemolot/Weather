package com.weather.app.data.db.entity

import androidx.room.*

@Entity(indices = [Index("name", unique = true)])
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name", )
    val name: String
)

@Dao
interface CityDao {
    @Query("SELECT * FROM CityEntity")
    suspend fun getAll(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityEntity)

    @Query("DELETE FROM CityEntity WHERE name = :cityName")
    suspend fun removeByName(cityName: String)
}