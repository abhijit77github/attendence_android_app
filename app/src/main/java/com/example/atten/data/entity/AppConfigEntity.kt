package com.example.atten.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "app_config")
@JsonClass(generateAdapter = true)
data class AppConfigEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    
    val endpoint: String = "",
    val httpMethod: String = "GET", // GET or POST
    
    val userId: String = "",
    val userEmail: String = "",
    
    // JSON payload parameters (stored as JSON string)
    val jsonPayload: String = "{}",
    
    // Success criteria
    val successCriteriaJsonPath: String = "", // e.g., "response.status"
    val successCriteriaValue: String = "", // e.g., "success"
    
    // Failure criteria
    val failureCriteriaJsonPath: String = "",
    val failureCriteriaValue: String = "",
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
