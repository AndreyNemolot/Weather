package com.weather.app.domain.model

data class CurrentWeatherDomainModel(
    val currentTemperature: Double? = null,
    val city: String? = null,
    val lon: Double? = null,
    val lat: Double? = null
)