package com.idyllic.core_imp.di

import com.idyllic.core_imp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppInterceptor @Inject constructor(
    private val idProvider: IdProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val imei = idProvider.getIMEI()
        val macAddress = idProvider.getMacAddress()
        val ipAddress = idProvider.getLocalIpAddress()
        Timber.d("IMEI: $imei")
        Timber.d("MAC: $macAddress , $ipAddress")
        val original: Request = chain.request()
        val requestBuilder: Request.Builder =
            original.newBuilder().header("x-device-type", "android")
                .header("x-device-model", idProvider.getDeviceNameAndOSVersion())
                .header("x-device-uid", idProvider.getDeviceID())
                .header("x-app-version", idProvider.getVersionName())
                .header("x-app-build", idProvider.getVersionCode().toString())
                .header("x-network-type", idProvider.getNetwork())

        if (BuildConfig.DEBUG) {
            requestBuilder.header("x-debug", "1")
        }
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
