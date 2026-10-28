package com.example.atten.data.repository

import com.example.atten.data.entity.AppConfigEntity
import com.example.atten.data.entity.LocationEntity
import com.example.atten.data.entity.RequestLogEntity
import com.example.atten.network.ApiService
import com.example.atten.network.NetworkClient
import com.example.atten.util.JsonPathUtils
import com.example.atten.util.LocationUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.withContext
import kotlin.coroutines.Dispatchers
import java.net.URL

data class RequestResult(
    val statusCode: Int,
    val responseBody: String,
    val isSuccess: Boolean,
    val errorMessage: String = "",
    val isNetworkError: Boolean = false,
    val usedLatitude: Double = 0.0,
    val usedLongitude: Double = 0.0
)

class RequestService(
    private val configRepository: ConfigRepository,
    private val locationRepository: LocationRepository,
    private val requestLogRepository: RequestLogRepository
) {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    
    suspend fun sendRequest(
        config: AppConfigEntity,
        location: LocationEntity
    ): RequestResult = withContext(Dispatchers.IO) {
        try {
            // Randomize location coordinates (within 100 meters)
            val (randomLat, randomLon) = LocationUtils.getRandomizedLocation(
                location.latitude,
                location.longitude,
                100
            )
            
            // Build request payload
            val payload = mutableMapOf<String, Any>()
            payload["userId"] = config.userId
            payload["userEmail"] = config.userEmail
            payload["latitude"] = randomLat
            payload["longitude"] = randomLon
            
            // Add any additional JSON payload parameters
            try {
                val additionalParams = moshi.adapter<Map<String, Any>>(Map::class.java)
                    .fromJson(config.jsonPayload)
                additionalParams?.let { payload.putAll(it) }
            } catch (e: Exception) {
                // Ignore if JSON payload is empty or invalid
            }
            
            // Extract base URL from endpoint
            val baseUrl = extractBaseUrl(config.endpoint)
            val apiService = NetworkClient.getApiService(baseUrl)
            
            // Make the request
            val response = when (config.httpMethod.uppercase()) {
                "POST" -> apiService.post(config.endpoint, payload)
                "GET" -> {
                    // Convert to query parameters
                    val queryParams = payload.mapValues { it.value.toString() }
                    apiService.get(config.endpoint, queryParams)
                }
                else -> apiService.get(config.endpoint)
            }
            
            val responseBody = response.body()?.toString() ?: "{}"
            val statusCode = response.code()
            
            // Check success/failure criteria
            val isSuccess = if (config.successCriteriaJsonPath.isNotEmpty()) {
                JsonPathUtils.checkResponseCriteria(
                    response.body(),
                    config.successCriteriaJsonPath,
                    config.successCriteriaValue
                )
            } else {
                statusCode in 200..299
            }
            
            val requestLog = RequestLogEntity(
                configId = config.id,
                locationId = location.id,
                locationName = location.locationName,
                usedLatitude = randomLat,
                usedLongitude = randomLon,
                httpMethod = config.httpMethod,
                requestUrl = config.endpoint,
                requestBody = moshi.adapter<Map<String, Any>>(Map::class.java).toJson(payload),
                statusCode = statusCode,
                responseBody = responseBody,
                isSuccess = isSuccess,
                isNetworkError = false
            )
            
            // Save log to database
            requestLogRepository.addRequestLog(requestLog)
            
            RequestResult(
                statusCode = statusCode,
                responseBody = responseBody,
                isSuccess = isSuccess,
                usedLatitude = randomLat,
                usedLongitude = randomLon
            )
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error occurred"
            
            val requestLog = RequestLogEntity(
                configId = config.id,
                locationId = location.id,
                locationName = location.locationName,
                statusCode = 0,
                responseBody = "",
                isSuccess = false,
                errorMessage = errorMessage,
                isNetworkError = true
            )
            
            requestLogRepository.addRequestLog(requestLog)
            
            RequestResult(
                statusCode = 0,
                responseBody = "",
                isSuccess = false,
                errorMessage = errorMessage,
                isNetworkError = true
            )
        }
    }
    
    private fun extractBaseUrl(fullUrl: String): String {
        return try {
            val url = URL(fullUrl)
            "${url.protocol}://${url.host}${if (url.port != -1) ":${url.port}" else ""}"
        } catch (e: Exception) {
            "https://api.example.com"
        }
    }
}
