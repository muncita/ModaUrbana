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
 * Todas las rutas deben incluir el prefijo /api/
 */
interface ApiService {

    // ------------------------------------------------------------
    //                     AUTENTICACIÓN
    // ------------------------------------------------------------

    @POST("api/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @POST("api/auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): LoginResponse

    @GET("api/auth/profile")
    suspend fun getCurrentUser(
        @Header("Authorization") bearerToken: String
    ): ApiResponse<UserResponseDto>


    // ------------------------------------------------------------
    //                     PRODUCTOS
    // ------------------------------------------------------------

    @GET("api/producto")
    suspend fun getProductos(): ApiResponse<List<ProductDto>>

    @GET("api/producto/{id}")
    suspend fun getProductoPorId(
        @Path("id") id: String
    ): ApiResponse<ProductDto>
}
