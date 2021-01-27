package com.weather.app.data.model

data class CurrentWeatherServerModel(
    val cod: Int?,
    val message: String?,
    val main: MainModel?,
    val coord: Coord?,
    val name: String?
) {
    data class MainModel(
        val temp: Double?,
        val feels_like: Double?,
        val temp_min: Double?,
        val temp_max: Double?,
        val pressure: Int?,
        val humidity: Int?
    )

    data class Coord(
        val lon: Double?,
        val lat: Double?
    )
}