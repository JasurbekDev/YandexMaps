package com.idyllic.core_imp.di

import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.core_imp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HostInterceptor @Inject constructor(
    private val secureStorageManager: SecureStorageManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val baseUrl: String = if (BuildConfig.DEBUG) {
            if (secureStorageManager.isTestServer) BuildConfig.BASE_URL_TEST
            else secureStorageManager.workingBaseUrl
        } else secureStorageManager.workingBaseUrl

//        val baseUrl: String = secureStorageManager.workingBaseUrl

        val host: HttpUrl? = baseUrl.toHttpUrlOrNull()

        val newRequest = host?.let {
            val newUrl = chain
                .request()
                .url
                .newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()

            return@let chain.request().newBuilder().url(newUrl).build()

        } ?: chain.request()

        return chain.proceed(newRequest)
    }
}