package com.example.atten.data.repository

import com.example.atten.data.dao.AppConfigDao
import com.example.atten.data.entity.AppConfigEntity
import kotlinx.coroutines.flow.Flow

class ConfigRepository(private val appConfigDao: AppConfigDao) {
    suspend fun getConfig(): AppConfigEntity? = appConfigDao.getConfig(1)
    
    fun getConfigFlow(): Flow<AppConfigEntity?> = appConfigDao.getConfigFlow(1)
    
    suspend fun saveConfig(config: AppConfigEntity) {
        val existing = appConfigDao.getConfig(1)
        if (existing != null) {
            appConfigDao.updateConfig(config.copy(id = 1))
        } else {
            appConfigDao.insertConfig(config.copy(id = 1))
        }
    }
    
    suspend fun hasConfig(): Boolean = appConfigDao.hasConfig()
    
    suspend fun deleteConfig() {
        val config = appConfigDao.getConfig(1)
        config?.let { appConfigDao.deleteConfig(it) }
    }
}
