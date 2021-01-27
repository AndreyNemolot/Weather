package com.weather.app.data.repository

import com.weather.app.data.model.CurrentWeatherServerModel
import com.weather.app.data.model.DailyWeatherServerModel
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

interface WeatherRepository {
    suspend fun getCurrentWeatherByCity(place: String): CurrentWeatherServerModel

    suspend fun getCurrentWeatherByCoord(lat: Double, lon: Double): CurrentWeatherServerModel

    suspend fun getDailyWeather(lat: Double, lon: Double): DailyWeatherServerModel

}