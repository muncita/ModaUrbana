package com.example.modaurbana.data.remote

import com.example.modaurbana.models.*
import retrofit2.http.*

interface ApiService {

    // --- REGISTRO ---
    @POST("auth/signup")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    // --- LOGIN ---
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    // --- PERFIL (requiere token) ---
    @GET("auth/me")
    suspend fun me(@Header("Authorization") bearer: String): User
}
