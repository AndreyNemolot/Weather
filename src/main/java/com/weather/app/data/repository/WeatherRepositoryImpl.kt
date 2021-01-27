package com.weather.app.data.repository

import androidx.core.net.toUri
import com.weather.app.data.ApiData.API_KEY
import com.weather.app.data.ApiData.CURRENT_WEATHER
import com.weather.app.data.ApiData.DAILY_WEATHER
import com.weather.app.data.model.CurrentWeatherServerModel
import com.weather.app.data.model.DailyWeatherServerModel
import com.weather.app.domain.network.Network
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.util.concurrent.Flow

class WeatherRepositoryImpl(private val network: Network) : WeatherRepository {

    override suspend fun getCurrentWeatherByCity(place: String): CurrentWeatherServerModel {
        val url = CURRENT_WEATHER.toUri().buildUpon()
            .appendQueryParameter("q", place)
            .appendQueryParameter("units", "metric")
            .appendQueryParameter("appid", API_KEY)
            .build().toString()
        val response = network.get<CurrentWeatherServerModel>(url)
        if (response.cod !in 200..299) throw IOException(response.message)
        return response
    }

    override suspend fun getCurrentWeatherByCoord(lat: Double, lon: Double): CurrentWeatherServerModel {
        val url = CURRENT_WEATHER.toUri().buildUpon()
            .appendQueryParameter("lat", lat.toString())
            .appendQueryParameter("lon", lon.toString())
            .appendQueryParameter("units", "metric")
            .appendQueryParameter("appid", API_KEY)
            .build().toString()
        val response = network.get<CurrentWeatherServerModel>(url)
        if (response.cod !in 200..299) throw IOException(response.message)
        return response
    }

    override suspend fun getDailyWeather(lat: Double, lon: Double): DailyWeatherServerModel {
        val url = DAILY_WEATHER.toUri().buildUpon()
            .appendQueryParameter("lat", lat.toString())
            .appendQueryParameter("lon", lon.toString())
            .appendQueryParameter("exclude", "week")
            .appendQueryParameter("units", "metric")
            .appendQueryParameter("appid", API_KEY)
            .build().toString()
        val response = network.get<DailyWeatherServerModel>(url)
        return response
    }



//    &units=imperial
}