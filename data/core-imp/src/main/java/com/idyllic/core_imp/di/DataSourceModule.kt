package com.idyllic.core_imp.di

import com.idyllic.core_api.source.CoreDataSource
import com.idyllic.core_imp.source.CoreDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsCoreDataSource(source: CoreDataSourceImp): CoreDataSource

}