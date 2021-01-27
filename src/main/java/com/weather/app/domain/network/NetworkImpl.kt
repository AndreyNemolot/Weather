package com.weather.app.domain.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NetworkImpl : Network() {

    private val client = HttpClientProvider.get()
    private val gson = GsonProvider.get()

    override suspend fun <T> requestJson(url: String, type: Type): T = withContext(IO) {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        client.newCall(request).await().parseToModel(type)
    }

    private suspend fun Call.await(): Response {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isCancelled) return
                    continuation.resumeWithException(e)
                }
            })

            continuation.invokeOnCancellation {
                try {
                    cancel()
                } catch (ex: Throwable) { }
            }
        }
    }

    private suspend fun <T> Response.parseToModel(type: Type): T =
        withContext(Dispatchers.Unconfined) {
            val body = body() ?: throw NullPointerException(toString())
            gson.fromJson(body.charStream(), type)
        }

}