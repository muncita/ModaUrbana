package com.example.modaurbana.data.remote

import com.example.modaurbana.models.*
import com.example.modaurbana.models.LoginResponse
import com.example.modaurbana.models.RegisterRequest
import com.example.modaurbana.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @POST("auth/signup")
    suspend fun register(
        @Body body: RegisterRequest
    ): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): UserResponse
}
