package com.weather.app.domain.model

data class DailyWeatherDomainModel(
    val days: List<Day>? = null
) {
    data class Day(
        val date: Long? = null,
        val dayTemperature: Double? = null,
        val maxTemperature: Double? = null,
        val minTemperature: Double? = null,
        val pressure: Int? = null,
        val humidity: Int? = null,
    )
}