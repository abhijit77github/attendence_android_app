package com.example.atten.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import retrofit2.http.QueryMap
import retrofit2.http.HeaderMap

interface ApiService {
    @GET
    suspend fun get(
        @Url url: String,
        @QueryMap params: Map<String, String> = emptyMap(),
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<Map<String, Any>>

    @POST
    suspend fun post(
        @Url url: String,
        @Body body: Map<String, Any>,
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<Map<String, Any>>
}
