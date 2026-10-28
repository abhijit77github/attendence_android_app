package com.example.atten.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkClient {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private var apiService: ApiService? = null

    fun getApiService(baseUrl: String = "https://api.example.com/"): ApiService {
        return if (apiService == null || needsNewClient(baseUrl)) {
            createRetrofitClient(baseUrl).also { apiService = it }
        } else {
            apiService!!
        }
    }

    private fun createRetrofitClient(baseUrl: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    private fun needsNewClient(baseUrl: String): Boolean {
        // Implement logic to check if we need a new client for a different base URL
        return false
    }
}
