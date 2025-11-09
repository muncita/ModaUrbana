package com.example.modaurbana.repository

import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.models.LoginResponse
import com.example.modaurbana.models.UserResponse

/**
 * Repositorio que maneja las peticiones de usuario:
 * login, datos del perfil, etc.
 */
class UserRepository {
    private val api = RetrofitClient.instance

    suspend fun getCurrentUser(token: String): UserResponse {
        return api.getCurrentUser("Bearer $token")
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(mapOf("email" to email, "password" to password))
    }

    suspend fun register(name: String, email: String, password: String): LoginResponse {
        return api.register(mapOf("name" to name, "email" to email, "password" to password))
    }
}
