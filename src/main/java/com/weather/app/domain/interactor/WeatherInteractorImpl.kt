package com.weather.app.domain.interactor

import com.weather.app.data.repository.WeatherRepository
import com.weather.app.domain.model.CurrentWeatherDomainModel
import com.weather.app.domain.model.DailyWeatherDomainModel

class WeatherInteractorImpl(private val repository: WeatherRepository) : WeatherInteractor {

    override suspend fun getDailyWeatherByCity(lat: Double, lon: Double): DailyWeatherDomainModel {
        val response = repository.getDailyWeather(lat, lon)
        val daysList = response.daily?.map {
            DailyWeatherDomainModel.Day(
                date = it.dt,
                dayTemperature = it.temp?.day,
                maxTemperature = it.temp?.max,
                minTemperature = it.temp?.min,
                pressure = it.pressure,
                humidity = it.humidity
            )
        }
        return DailyWeatherDomainModel(daysList)
    }

    override suspend fun getCurrentWeatherByCity(city: String): CurrentWeatherDomainModel {
        val response = repository.getCurrentWeatherByCity(city)
        return CurrentWeatherDomainModel(
            city = response.name,
            currentTemperature = response.main?.temp,
            lon = response.coord?.lon,
            lat = response.coord?.lat
        )
    }

    override suspend fun getCurrentWeatherByCoord(lat: Double, lon: Double): CurrentWeatherDomainModel {
        val response = repository.getCurrentWeatherByCoord(lat, lon)
        return CurrentWeatherDomainModel(
            city = response.name,
            currentTemperature = response.main?.temp,
            lon = response.coord?.lon,
            lat = response.coord?.lat
        )
    }
}