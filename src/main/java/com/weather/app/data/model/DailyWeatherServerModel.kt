package com.weather.app.data.model

data class DailyWeatherServerModel(
    val cod: Int?,
    val message: String?,
    val daily: List<DailyModel>?
) {
    data class DailyModel(
        val dt: Long?,
        val temp: TempModel?,
        val pressure: Int?,
        val humidity: Int?
    )

    data class TempModel(
        val day: Double?,
        val min: Double?,
        val max: Double?,
        val night: Double?,
    )

}