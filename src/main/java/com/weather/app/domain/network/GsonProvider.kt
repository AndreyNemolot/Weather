package com.weather.app.domain.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object GsonProvider {

    private val gson = Gson()

    fun get(): Gson {
        return gson
    }
}