package com.idyllic.core_imp.di

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.core_imp.model.RefreshToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RefreshTokenInterceptor @Inject constructor(
    private val secureStorageManager: SecureStorageManager,
    @ApplicationContext val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val originalResponse = chain.proceed(request)

        if (originalResponse.code != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return originalResponse
        }

        val refreshToken = secureStorageManager.refreshToken.takeIf(String::isNotEmpty)
            ?: run {
                return originalResponse
            }

        originalResponse.close()
        val response = requestNewAccessToken(chain, refreshToken)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            secureStorageManager.clearAll()
            return response
        }

        if (!response.isSuccessful) {
            return response
        }

        response.body.let {
            val gson = Gson()
            gson.fromJson(
                it.string(),
                RefreshToken::class.java
            )
        }?.let {
            secureStorageManager.token = it.token.toString()
            secureStorageManager.expiresIn = it.expiresIn ?: 1
        }

        return response
    }

    private fun requestNewAccessToken(
        chain: Interceptor.Chain,
        refreshToken: String
    ): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $refreshToken")
//            .url("LINK_TO_REQUEST_NEW_TOKEN")
            .post("".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return chain.proceed(request)
    }

    private fun retryPreviousRequest(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()
        return chain.proceed(request)
    }
}