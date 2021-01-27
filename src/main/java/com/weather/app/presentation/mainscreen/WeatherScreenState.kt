package com.weather.app.presentation.mainscreen

import com.weather.app.domain.model.CurrentWeatherDomainModel
import com.weather.app.domain.model.DailyWeatherDomainModel

internal data class State(
    val weatherScreenState: WeatherScreenState = WeatherScreenState.NewCityAdd
)

internal sealed class WeatherScreenState {
    class Success(
        val currentWeather: CurrentWeatherDomainModel,
        val dailyWeather: DailyWeatherDomainModel
    ) : WeatherScreenState()

    object Loading : WeatherScreenState()
    object NewCityAdd : WeatherScreenState()
    object Error: WeatherScreenState()
}