package com.idyllic.map_imp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idyllic.map_imp.local.MapServiceDao
import com.idyllic.map_imp.model.LocationEntry

@Database(
    entities = [LocationEntry::class], version = 1
)
abstract class MapDatabase : RoomDatabase() {
    abstract fun mapServiceDao(): MapServiceDao
}