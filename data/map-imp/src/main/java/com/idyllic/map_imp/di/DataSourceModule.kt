package com.idyllic.map_imp.di

import com.idyllic.map_api.source.MapDataSource
import com.idyllic.map_imp.source.MapDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindsMapDataSource(source: MapDataSourceImp): MapDataSource
}