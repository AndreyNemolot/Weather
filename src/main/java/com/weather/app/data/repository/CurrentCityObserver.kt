package com.weather.app.data.repository

import kotlinx.coroutines.flow.MutableStateFlow

object CurrentCityObserver {

    var flow = MutableStateFlow("")
}