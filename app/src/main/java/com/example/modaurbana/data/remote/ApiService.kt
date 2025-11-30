package com.example.modaurbana.data.remote

import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.ProductDto
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.models.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Wrapper genérico de respuestas del backend Nest:
 * { "success": true, "message": "...", "data": ... }
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)

interface ApiService {

    // ---------- AUTH ----------
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): LoginResponse

    @GET("auth/profile")
    suspend fun getCurrentUser(
        @Header("Authorization") bearerToken: String
    ): ApiResponse<UserResponseDto>

    // ---------- PRODUCTOS ----------
    @GET("products")
    suspend fun getProductos(): ApiResponse<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductoPorId(
        @Path("id") id: String
    ): ApiResponse<ProductDto>
}
