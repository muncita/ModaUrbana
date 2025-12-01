package com.example.modaurbana.repository

import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.models.RegisterRequest

class UserRepository {

    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): LoginResponse {
        val body = LoginRequest(email = email, password = password)
        return api.login(body)
    }

    suspend fun register(nombre: String, email: String, password: String): LoginResponse {
        val body = RegisterRequest(
            nombre = nombre,
            email = email,
            password = password,
            role = "CLIENTE" // por si acaso, aunque ya tiene default
        )
        return api.register(body)
    }

    suspend fun getCurrentUser(token: String): UserResponseDto {
        val resp = api.getCurrentUser("Bearer $token")
        return resp.data ?: error("Respuesta de perfil sin data")
    }
}
