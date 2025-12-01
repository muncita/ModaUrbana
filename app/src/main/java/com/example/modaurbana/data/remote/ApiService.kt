package com.example.modaurbana.data.remote

import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.ProductDto
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.models.ApiResponse
import com.example.modaurbana.models.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Contrato de comunicación con el backend NestJS.
 * Todas las respuestas vienen envueltas en:
 * {
 *   "success": true,
 *   "message": "...",
 *   "data": ...
 * }
 */
interface ApiService {

    // ------------------------------------------------------------
    //                      AUTENTICACIÓN
    // ------------------------------------------------------------

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


    // ------------------------------------------------------------
    //                       PRODUCTOS
    //      (coincide EXACTO con tus rutas Nest: /producto)
    // ------------------------------------------------------------

    @GET("producto")
    suspend fun getProductos(): ApiResponse<List<ProductDto>>

    @GET("producto/{id}")
    suspend fun getProductoPorId(
        @Path("id") id: String
    ): ApiResponse<ProductDto>
}
