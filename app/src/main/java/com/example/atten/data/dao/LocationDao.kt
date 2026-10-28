package com.example.atten.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.atten.data.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations WHERE configId = :configId")
    suspend fun getLocations(configId: Int): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE configId = :configId")
    fun getLocationsFlow(configId: Int): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocation(id: Int): LocationEntity?

    @Insert
    suspend fun insertLocation(location: LocationEntity): Long

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("DELETE FROM locations WHERE configId = :configId")
    suspend fun deleteLocationsByConfig(configId: Int)
}
