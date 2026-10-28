package com.example.atten.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "locations",
    foreignKeys = [
        ForeignKey(
            entity = AppConfigEntity::class,
            parentColumns = ["id"],
            childColumns = ["configId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["configId"])]
)
@JsonClass(generateAdapter = true)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val configId: Int = 1,
    
    val locationName: String = "",
    
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    
    val createdAt: Long = System.currentTimeMillis()
)
