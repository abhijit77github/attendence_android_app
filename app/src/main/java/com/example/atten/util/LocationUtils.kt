package com.example.atten.util

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.random.Random

object LocationUtils {
    private const val EARTH_RADIUS_METERS = 6371000.0
    
    /**
     * Generate random coordinates within 100 meters of the given lat/long
     */
    fun getRandomizedLocation(latitude: Double, longitude: Double, radiusMeters: Int = 100): Pair<Double, Double> {
        val random = Random(System.currentTimeMillis())
        
        // Generate random bearing (0-360 degrees)
        val bearing = random.nextDouble() * 360
        
        // Generate random distance within radius (0 to radiusMeters)
        val distance = random.nextDouble() * radiusMeters
        
        return getDestinationPoint(latitude, longitude, bearing, distance)
    }
    
    /**
     * Calculate destination point given origin, bearing, and distance
     * Using the haversine formula
     */
    private fun getDestinationPoint(
        latitude: Double,
        longitude: Double,
        bearing: Double,
        distance: Double
    ): Pair<Double, Double> {
        val angularDistance = distance / EARTH_RADIUS_METERS
        val bearingRad = Math.toRadians(bearing)
        val latRad = Math.toRadians(latitude)
        val lonRad = Math.toRadians(longitude)
        
        val latDest = Math.asin(
            sin(latRad) * cos(angularDistance) +
                cos(latRad) * sin(angularDistance) * cos(bearingRad)
        )
        
        val lonDest = lonRad + atan2(
            sin(bearingRad) * sin(angularDistance) * cos(latRad),
            cos(angularDistance) - sin(latRad) * sin(latDest)
        )
        
        return Pair(
            Math.toDegrees(latDest),
            Math.toDegrees(lonDest)
        )
    }
}
