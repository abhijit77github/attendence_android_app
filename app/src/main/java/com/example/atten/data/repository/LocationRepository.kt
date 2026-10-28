package com.example.atten.data.repository

import com.example.atten.data.dao.LocationDao
import com.example.atten.data.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun getLocations(configId: Int): List<LocationEntity> = 
        locationDao.getLocations(configId)
    
    fun getLocationsFlow(configId: Int): Flow<List<LocationEntity>> = 
        locationDao.getLocationsFlow(configId)
    
    suspend fun getLocation(id: Int): LocationEntity? = 
        locationDao.getLocation(id)
    
    suspend fun addLocation(location: LocationEntity): Long = 
        locationDao.insertLocation(location)
    
    suspend fun updateLocation(location: LocationEntity) = 
        locationDao.updateLocation(location)
    
    suspend fun deleteLocation(location: LocationEntity) = 
        locationDao.deleteLocation(location)
    
    suspend fun deleteLocationsByConfig(configId: Int) = 
        locationDao.deleteLocationsByConfig(configId)
}
