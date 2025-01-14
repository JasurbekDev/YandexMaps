package com.idyllic.map_imp.di

import android.content.Context
import androidx.room.Room
import com.idyllic.map_imp.database.MapDatabase
import com.idyllic.map_imp.local.MapServiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun mapDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MapDatabase::class.java, "MapDatabase"
    ).build()

    @Provides
    fun mapServiceDao(mapDatabase: MapDatabase): MapServiceDao = mapDatabase.mapServiceDao()

}