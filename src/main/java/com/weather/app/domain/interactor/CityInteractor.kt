package com.weather.app.domain.interactor

import com.weather.app.domain.model.City

interface CityInteractor {
    suspend fun addCity(name: String, isCurrent: Boolean = false)
    suspend fun removeCity(name: String)
    suspend fun getAllCity(): List<City>
    fun setCurrentCity(name: String)
    fun getCurrentCity(): String
}