package com.idyllic.core_imp.di

import android.content.Context
import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.core_imp.util.SecureStorageManagerImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SourceStorageModule {
    @Provides
    @Singleton
    fun providerSecureStorageManager(
        @ApplicationContext context: Context
    ): SecureStorageManager = SecureStorageManagerImp(context)
}