package com.weather.app.data.repository

import com.weather.app.data.model.CityModel
import kotlinx.coroutines.flow.StateFlow

interface CityRepository {
    suspend fun addCity(name: String)
    suspend fun removeCity(name: String)
    suspend fun getAllCity(): List<CityModel>
    fun setCurrentCity(name: String)
    fun removeCurrentCity()
    fun getCurrentCity(): String
    val currentCityChangerFlow: StateFlow<String>

}