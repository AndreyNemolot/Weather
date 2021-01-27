package com.weather.app.presentation.mainscreen

sealed class MainCommand {
    object RequestCoordinates: MainCommand()
}