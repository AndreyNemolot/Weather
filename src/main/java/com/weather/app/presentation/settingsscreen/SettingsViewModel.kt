package com.weather.app.presentation.settingsscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.WeatherApplication
import com.weather.app.data.repository.CityRepositoryImpl
import com.weather.app.domain.interactor.CityInteractorImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val cityInteractor = CityInteractorImpl(
        CityRepositoryImpl(application as WeatherApplication)
    )

    private var state: State = State()
    internal val stateFlow = MutableStateFlow(State())
    private var job: Job? = null

    init {
        refresh()
    }

    fun selectCity(name: String) {
        cityInteractor.setCurrentCity(name)
        refresh()
    }

    fun removeCity(name: String) {
        job?.cancel()
        job = viewModelScope.launch {
            cityInteractor.removeCity(name)
            refresh()
        }
    }

    fun addCity(name: String) {
        job?.cancel()
        job = viewModelScope.launch {
            cityInteractor.addCity(name, true)
            refresh0()
        }

    }

    private fun refresh() {
        job?.cancel()
        job = viewModelScope.launch {
            refresh0()
        }
    }

    private suspend fun refresh0() {
        val cityList = cityInteractor.getAllCity()
        updateState {
            it.copy(currentState = CityScreenState.Success(cityList))
        }
    }

    private fun updateState(state0: (State) -> State) {
        state = state0(state)
        stateFlow.value = state
    }

}