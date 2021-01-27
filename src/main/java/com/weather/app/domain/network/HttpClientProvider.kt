package com.weather.app.domain.network

import android.app.Application
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


object HttpClientProvider {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5000, TimeUnit.MILLISECONDS)
        .readTimeout(40000, TimeUnit.MILLISECONDS)
        .writeTimeout(15000, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    fun get(): OkHttpClient {
        return okHttpClient
    }
}