package com.example.modaurbana.data.remote

import com.example.modaurbana.models.*
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.models.RegisterRequest
import com.example.modaurbana.models.UserResponse
import com.example.modaurbana.data.remote.dto.ApiResponseDto
import com.example.modaurbana.data.remote.dto.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("producto")
    suspend fun getProductos(): ApiResponseDto<List<ProductDto>>

    @GET("producto/{id}")
    suspend fun getProductoPorId(
        @Path("id") id: String
    ): ApiResponseDto<ProductDto>
}