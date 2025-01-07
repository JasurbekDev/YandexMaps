package com.idyllic.core_imp.di

import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.core_imp.BuildConfig
import com.idyllic.core_imp.model.BaseResponse
import com.idyllic.core_imp.model.RefreshToken
import com.idyllic.core_imp.remote.CoreService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RefreshTokenAuthenticator @Inject constructor(
    private val secureStorageManager: SecureStorageManager,
    private val chuckerInterceptor: ChuckerInterceptor,
    private val gsonConverterFactory: GsonConverterFactory,
    private val hostInterceptor: HostInterceptor,
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val appInterceptor: AppInterceptor,
) : Authenticator {
    private val list = HashMap<String, Response>()
    private var isHave: Boolean = false
    override fun authenticate(route: Route?, response: Response): Request? =
        synchronized(secureStorageManager) {
            list[response.request.url.toString()] = response

            return runBlocking {
                if (secureStorageManager.refreshToken.isBlank()) {
                    return@runBlocking null
                }
                val newToken: retrofit2.Response<BaseResponse<RefreshToken>> = getNewResponse()
                if (!newToken.isSuccessful || newToken.body() == null || newToken.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.d("GGG", "new token response")
                    secureStorageManager.clearAll()
                    return@runBlocking null
                }
                Log.d("GGG", "url: ${response.request.url}   route: ${route?.address?.url}")
                newToken.body()?.let {
                    secureStorageManager.token = it.data?.token.toString()
                    secureStorageManager.expiresIn = it.data?.expiresIn ?: 1
                    response.request.newBuilder().header(
                        "Authorization", "Bearer ${it.data?.token}"
                    ).build()
                }
            }
        }


    private suspend fun getNewResponse(): retrofit2.Response<BaseResponse<RefreshToken>> {
        val headerInterceptor = Interceptor {
            val request = it.request().newBuilder()
                .header("Authorization", "Bearer ${secureStorageManager.refreshToken}").build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(hostInterceptor)
            addInterceptor(appInterceptor)
            addInterceptor(headerInterceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
                addInterceptor(chuckerInterceptor)
            }
        }.build()

        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_PROD_API)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(okHttpClient).build()
        val service = retrofit.create(CoreService::class.java)
        return service.authRefreshToken()
    }
}