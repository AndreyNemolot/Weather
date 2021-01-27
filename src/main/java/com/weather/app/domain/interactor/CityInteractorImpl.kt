package com.weather.app.domain.interactor

import com.weather.app.data.repository.CityRepository
import com.weather.app.domain.model.City

class CityInteractorImpl(private val repository: CityRepository): CityInteractor {

    override suspend fun addCity(name: String, isCurrent: Boolean) {
        repository.addCity(name = name)
        if (isCurrent) {
            repository.setCurrentCity(name)
        }
    }

    override suspend fun removeCity(name: String) {
        repository.removeCity(name)
        if (repository.getCurrentCity() == name) {
            repository.removeCurrentCity()
            repository.getAllCity().apply {
                firstOrNull()?.name?.let { firstCity ->
                    setCurrentCity(firstCity)
                }
            }
        }
    }

    override suspend fun getAllCity(): List<City> {
        val currentCity = repository.getCurrentCity()
        return repository.getAllCity().map {
            City(
                name = it.name,
                isCurrent = currentCity == it.name
            )
        }
    }

    override fun setCurrentCity(name: String) {
        repository.setCurrentCity(name)
    }

    override fun getCurrentCity(): String {
        return repository.getCurrentCity()
    }
}