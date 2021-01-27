package com.weather.app.domain.interactor

import com.weather.app.domain.model.CurrentWeatherDomainModel
import com.weather.app.domain.model.DailyWeatherDomainModel

interface WeatherInteractor {
    suspend fun getDailyWeatherByCity(lat: Double, lon: Double): DailyWeatherDomainModel

    suspend fun getCurrentWeatherByCity(city: String): CurrentWeatherDomainModel

    suspend fun getCurrentWeatherByCoord(lat: Double, lon: Double): CurrentWeatherDomainModel
}