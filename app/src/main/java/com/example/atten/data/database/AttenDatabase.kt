package com.example.atten.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.atten.data.dao.AppConfigDao
import com.example.atten.data.dao.LocationDao
import com.example.atten.data.dao.RequestLogDao
import com.example.atten.data.dao.TaskDao
import com.example.atten.data.entity.AppConfigEntity
import com.example.atten.data.entity.LocationEntity
import com.example.atten.data.entity.RequestLogEntity
import com.example.atten.data.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        AppConfigEntity::class,
        LocationEntity::class,
        RequestLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AttenDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun appConfigDao(): AppConfigDao
    abstract fun locationDao(): LocationDao
    abstract fun requestLogDao(): RequestLogDao

    companion object {
        @Volatile
        private var Instance: AttenDatabase? = null

        fun getDatabase(context: Context): AttenDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AttenDatabase::class.java,
                    "atten_database"
                ).build().also { Instance = it }
            }
        }
    }
}
