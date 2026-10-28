package com.example.atten.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "request_logs",
    foreignKeys = [
        ForeignKey(
            entity = AppConfigEntity::class,
            parentColumns = ["id"],
            childColumns = ["configId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["configId"]), Index(value = ["locationId"])]
)
@JsonClass(generateAdapter = true)
data class RequestLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val configId: Int = 1,
    val locationId: Int = 0,
    val locationName: String = "",
    
    // Actual coordinates used in request (with randomization applied)
    val usedLatitude: Double = 0.0,
    val usedLongitude: Double = 0.0,
    
    // Request details
    val httpMethod: String = "GET",
    val requestUrl: String = "",
    val requestBody: String = "",
    
    // Response details
    val statusCode: Int = 0,
    val responseBody: String = "",
    
    // Status
    val isSuccess: Boolean = false,
    val errorMessage: String = "",
    val isNetworkError: Boolean = false,
    
    val timestamp: Long = System.currentTimeMillis()
)
