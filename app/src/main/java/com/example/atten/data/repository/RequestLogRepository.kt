package com.example.atten.data.repository

import com.example.atten.data.dao.RequestLogDao
import com.example.atten.data.entity.RequestLogEntity
import kotlinx.coroutines.flow.Flow

class RequestLogRepository(private val requestLogDao: RequestLogDao) {
    suspend fun getRequestLogs(configId: Int, limit: Int = 100): List<RequestLogEntity> = 
        requestLogDao.getRequestLogs(configId, limit)
    
    fun getRequestLogsFlow(configId: Int, limit: Int = 100): Flow<List<RequestLogEntity>> = 
        requestLogDao.getRequestLogsFlow(configId, limit)
    
    suspend fun getRequestLogsLast7Days(configId: Int): List<RequestLogEntity> {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return requestLogDao.getRequestLogsLast7Days(configId, sevenDaysAgo)
    }
    
    fun getRequestLogsLast7DaysFlow(configId: Int): Flow<List<RequestLogEntity>> {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return requestLogDao.getRequestLogsLast7DaysFlow(configId, sevenDaysAgo)
    }
    
    suspend fun addRequestLog(log: RequestLogEntity): Long = 
        requestLogDao.insertRequestLog(log)
    
    suspend fun updateRequestLog(log: RequestLogEntity) = 
        requestLogDao.updateRequestLog(log)
    
    suspend fun deleteRequestLog(log: RequestLogEntity) = 
        requestLogDao.deleteRequestLog(log)
    
    suspend fun deleteLogsByConfig(configId: Int) = 
        requestLogDao.deleteLogsByConfig(configId)
}
