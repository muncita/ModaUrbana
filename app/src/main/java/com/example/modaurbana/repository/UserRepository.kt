package com.example.modaurbana.repository

import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.models.RegisterRequest

class UserRepository {

    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(LoginRequest(email = email, password = password))
    }

    suspend fun register(name: String, email: String, password: String): LoginResponse {
        return api.register(RegisterRequest(name = name, email = email, password = password))
    }

    suspend fun getCurrentUser(token: String): UserResponseDto {
        val resp = api.getCurrentUser("Bearer $token")
        return resp.data ?: error("Respuesta de perfil sin data")
    }
}
