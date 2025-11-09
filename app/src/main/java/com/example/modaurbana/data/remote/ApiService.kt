package com.example.modaurbana.data.remote

import com.example.modaurbana.models.LoginResponse
import com.example.modaurbana.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Define los endpoints de la API de Xano
 */
interface ApiService {

    // 🔹 Login
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): LoginResponse

    // 🔹 Registro
    @POST("auth/signup")
    suspend fun register(@Body body: Map<String, String>): LoginResponse

    // 🔹 Obtener usuario autenticado
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): UserResponse
}
