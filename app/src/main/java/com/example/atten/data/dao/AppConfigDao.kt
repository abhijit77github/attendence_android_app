package com.example.atten.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.atten.data.entity.AppConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppConfigDao {
    @Query("SELECT * FROM app_config WHERE id = :id")
    suspend fun getConfig(id: Int): AppConfigEntity?

    @Query("SELECT * FROM app_config WHERE id = :id")
    fun getConfigFlow(id: Int): Flow<AppConfigEntity?>

    @Insert
    suspend fun insertConfig(config: AppConfigEntity)

    @Update
    suspend fun updateConfig(config: AppConfigEntity)

    @Delete
    suspend fun deleteConfig(config: AppConfigEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM app_config LIMIT 1)")
    suspend fun hasConfig(): Boolean
}
