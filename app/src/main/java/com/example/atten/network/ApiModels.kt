package com.example.atten.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val statusCode: Int? = null,
    val body: Map<String, Any>? = null,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class DynamicJsonResponse(
    val response: Map<String, Any>? = null
)
