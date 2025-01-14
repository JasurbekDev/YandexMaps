package com.idyllic.map_imp.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idyllic.map_imp.model.LocationEntry

@Dao
interface MapServiceDao {

    @Query("SELECT * FROM LocationEntry")
    suspend fun getLocations(): List<LocationEntry>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertLocations(locations: List<LocationEntry>)

    @Query("DELETE FROM LocationEntry WHERE lat = :latitude AND lon = :longitude")
    suspend fun deleteByLatLon(latitude: Double, longitude: Double)

}