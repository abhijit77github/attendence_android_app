package com.example.atten.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.atten.data.entity.RequestLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestLogDao {
    @Query("SELECT * FROM request_logs WHERE configId = :configId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRequestLogs(configId: Int, limit: Int): List<RequestLogEntity>

    @Query("SELECT * FROM request_logs WHERE configId = :configId ORDER BY timestamp DESC LIMIT :limit")
    fun getRequestLogsFlow(configId: Int, limit: Int): Flow<List<RequestLogEntity>>

    @Query("SELECT * FROM request_logs WHERE configId = :configId AND timestamp >= :sevenDaysAgo ORDER BY timestamp DESC")
    suspend fun getRequestLogsLast7Days(configId: Int, sevenDaysAgo: Long): List<RequestLogEntity>

    @Query("SELECT * FROM request_logs WHERE configId = :configId AND timestamp >= :sevenDaysAgo ORDER BY timestamp DESC")
    fun getRequestLogsLast7DaysFlow(configId: Int, sevenDaysAgo: Long): Flow<List<RequestLogEntity>>

    @Query("SELECT * FROM request_logs WHERE id = :id")
    suspend fun getRequestLog(id: Int): RequestLogEntity?

    @Insert
    suspend fun insertRequestLog(log: RequestLogEntity): Long

    @Update
    suspend fun updateRequestLog(log: RequestLogEntity)

    @Delete
    suspend fun deleteRequestLog(log: RequestLogEntity)

    @Query("DELETE FROM request_logs WHERE configId = :configId")
    suspend fun deleteLogsByConfig(configId: Int)
}
