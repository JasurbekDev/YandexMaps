package com.idyllic.core_imp.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DdosInterceptor @Inject constructor() : Interceptor {
    private val HTTP_DDOS_ERROR = 429

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val headers: Headers = response.headers
        return if (response.code == HTTP_DDOS_ERROR) runBlocking {
            delay(100_000)
            response
        } else response
    }
}