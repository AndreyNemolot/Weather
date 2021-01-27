package com.weather.app.data.repository

import android.content.Context
import com.weather.app.WeatherApplication
import com.weather.app.data.db.entity.CityEntity
import com.weather.app.data.model.CityModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CityRepositoryImpl(application: WeatherApplication): CityRepository {

    private val dao = application.db.cityDao()
    private val pref = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val currentCityObserver = CurrentCityObserver

    override suspend fun addCity(name: String) {
        dao.insert(CityEntity(name = name))
    }

    override suspend fun removeCity(name: String) {
        dao.removeByName(name)
    }

    override suspend fun getAllCity(): List<CityModel> {
        return dao.getAll().map {
            CityModel(
                name = it.name
            )
        }
    }

    override fun setCurrentCity(name: String) {
        currentCityObserver.flow.tryEmit(name)
        with(pref.edit()) {
            putString(CURRENT_CITY_KEY, name)
            apply()
        }
    }

    override fun removeCurrentCity() {
        with(pref.edit()) {
            remove(CURRENT_CITY_KEY)
            apply()
        }
    }

    override fun getCurrentCity(): String {
        return pref.getString(CURRENT_CITY_KEY, "") ?: ""
    }

    override val currentCityChangerFlow: StateFlow<String>
        get() = currentCityObserver.flow.asStateFlow()

    companion object {
        private const val CURRENT_CITY_KEY = "CURRENT_CITY"
        private const val PREFS_NAME = "WEATHER_PREFS"
    }
}