package com.idyllic.map_imp.di

import com.idyllic.map_api.repository.MapRepository
import com.idyllic.map_api.repository.MapRepositoryDb
import com.idyllic.map_imp.repository.MapRepositoryDbImp
import com.idyllic.map_imp.repository.MapRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsMapRepository(repository: MapRepositoryImp): MapRepository

    @Binds
    fun bindsMapRepositoryDb(repository: MapRepositoryDbImp): MapRepositoryDb

}