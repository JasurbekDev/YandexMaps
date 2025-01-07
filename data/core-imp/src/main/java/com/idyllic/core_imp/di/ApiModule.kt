package com.idyllic.core_imp.di

import com.idyllic.core_imp.remote.CoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun providesCoreService(
        @AppServiceQualifier retrofit: Retrofit
    ): CoreService = retrofit.create(CoreService::class.java)
}