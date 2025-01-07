package com.idyllic.core_imp.di

import com.idyllic.core_api.repository.CoreRepository
import com.idyllic.core_imp.repository.CoreRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsCoreCoreRepository(source: CoreRepositoryImp): CoreRepository

}