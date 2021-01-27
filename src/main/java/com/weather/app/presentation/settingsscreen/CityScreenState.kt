package com.weather.app.presentation.settingsscreen

import com.weather.app.domain.model.City

internal data class State(
    val currentState: CityScreenState = CityScreenState.Loading
)

internal sealed class CityScreenState {
    class Success(
        val cityList: List<City>
    ) : CityScreenState()

    object Loading : CityScreenState()
    object Error: CityScreenState()
}

