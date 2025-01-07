package com.idyllic.core_imp.di


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.idyllic.core_imp.BuildConfig
import com.idyllic.core_imp.BuildConfig.BASE_URL_PROD_API
import com.idyllic.core_imp.util.NetworkConfig
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @AppServiceQualifier
    @ExperimentalSerializationApi
    fun provideRetrofitInstance(
        okhttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().client(okhttpClient).baseUrl(BASE_URL_PROD_API)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonConverterFactory).build()
    }

    @Provides
    fun gson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun gsonConvertFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun okHttpClient(
//        refreshTokenInterceptor: RefreshTokenInterceptor,
        refreshTokenAuthenticator: RefreshTokenAuthenticator,
        hostInterceptor: HostInterceptor,
        appInterceptor: AppInterceptor,
        tokenInterceptor: TokenInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient = with(OkHttpClient.Builder()) {
        callTimeout(NetworkConfig.CALL_TIMEOUT, TimeUnit.MILLISECONDS)
        connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        addInterceptor(appInterceptor)
        addInterceptor(tokenInterceptor)
        addInterceptor(hostInterceptor)
        authenticator(refreshTokenAuthenticator)
        if (BuildConfig.DEBUG) {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(chuckerInterceptor)
        }
        build()
    }

    @Provides
    fun cache(file: File): Cache {
        return Cache(file, 20 * 100 * 100)//(20)MB
    }

    @Provides
    fun file(@ApplicationContext context: Context): File {
        val file = File(context.cacheDir, "HttpCache")
        file.mkdirs()
        return file
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    fun provideChuckerCollector(
        @ApplicationContext context: Context
    ): ChuckerCollector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_WEEK,
    )

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context, chuckerCollector: ChuckerCollector
    ): ChuckerInterceptor =
        ChuckerInterceptor.Builder(context = context).collector(collector = chuckerCollector)
            .maxContentLength(550000L).redactHeaders(emptySet()).alwaysReadResponseBody(false)
            .build()

}