package com.example.modaurbana.repository

import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.models.LoginRequest
import com.example.modaurbana.models.LoginResponse
import com.example.modaurbana.models.RegisterRequest
import com.example.modaurbana.models.UserResponse


class UserRepository {
    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(LoginRequest(email = email, password = password))
    }

    suspend fun register(name: String, email: String, password: String): LoginResponse {
        return api.register(RegisterRequest(name = name, email = email, password = password))
    }

    suspend fun getCurrentUser(token: String): UserResponse {
        return api.getCurrentUser("Bearer $token")
    }
}
