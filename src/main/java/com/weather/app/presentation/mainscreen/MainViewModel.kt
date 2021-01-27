package com.weather.app.presentation.mainscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.WeatherApplication
import com.weather.app.data.repository.CityRepositoryImpl
import com.weather.app.data.repository.WeatherRepositoryImpl
import com.weather.app.domain.interactor.CityInteractorImpl
import com.weather.app.domain.interactor.WeatherInteractorImpl
import com.weather.app.domain.network.Network
import com.weather.app.domain.network.NetworkImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val network: Network = NetworkImpl()
    private val weatherInteractor = WeatherInteractorImpl(WeatherRepositoryImpl(network))
    private val cityRepository = CityRepositoryImpl(application as WeatherApplication)
    private val cityInteractor = CityInteractorImpl(cityRepository)

    private var state: State = State()
    private var isLocationRequested: Boolean = false
    private val _stateFlow = MutableStateFlow(State())
    private val _command = Channel<MainCommand>()
    internal val stateFlow = _stateFlow.asStateFlow()
    private var job: Job? = null
    val command = _command.receiveAsFlow()

    init {
        refresh(true)
        viewModelScope.launch {
            cityRepository.currentCityChangerFlow
                .drop(1)
                .collect {
                    loadByCity(it, true)
                }
        }
    }

    fun refresh(isInit: Boolean) {
        val currentCity = cityInteractor.getCurrentCity()
        when {
            currentCity.isNotEmpty() -> {
                loadByCity(currentCity, isInit)
            }
            !isLocationRequested -> {
                isLocationRequested = true
                job?.cancel()
                job = viewModelScope.launch {
                    _command.send(MainCommand.RequestCoordinates)
                    showAddCity()
                }
            }
        }
    }

    private fun showAddCity() {
        updateState {
            state.copy(
                weatherScreenState = WeatherScreenState.NewCityAdd
            )
        }
    }

    fun loadByLocation(lat: Double?, lon: Double?, isInit: Boolean) {
        load(isInit) {
            val currentModel = weatherInteractor.getCurrentWeatherByCoord(
                requireNotNull(lat),
                requireNotNull(lon)
            )
            val dailyModel = weatherInteractor.getDailyWeatherByCity(lat, lon)
            cityInteractor.addCity(requireNotNull(currentModel.city), true)
            updateState {
                state.copy(
                    weatherScreenState = WeatherScreenState.Success(currentModel, dailyModel)

                )
            }
        }
    }

    private fun loadByCity(city: String, isInit: Boolean) {
        load(isInit) {
            val currentWeather = weatherInteractor.getCurrentWeatherByCity(city)
            val dailyWeather = weatherInteractor.getDailyWeatherByCity(
                requireNotNull(currentWeather.lat),
                requireNotNull(currentWeather.lon)
            )
            updateState {
                it.copy(
                    weatherScreenState = WeatherScreenState.Success(currentWeather, dailyWeather)
                )
            }
        }
    }


    private fun load(isInit: Boolean, fu: suspend CoroutineScope.() -> Unit) {
        job = viewModelScope.launch {
            if (isInit) {
                updateState {
                    state.copy(weatherScreenState = WeatherScreenState.Loading)
                }
            }
            try {
                fu.invoke(this)
            } catch (e: Exception) {
                updateState {
                    state.copy(weatherScreenState = WeatherScreenState.Error)
                }
            }
        }
    }

    private fun updateState(state0: (State) -> State) {
        state = state0(state)
        _stateFlow.tryEmit(state)
    }

}