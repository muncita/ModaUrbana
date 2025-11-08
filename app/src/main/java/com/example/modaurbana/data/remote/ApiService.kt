package com.example.modaurbana.data.remote

import com.example.modaurbana.models.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/signup")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @GET("auth/me")
    suspend fun me(@Header("Authorization") bearer: String): User
}
