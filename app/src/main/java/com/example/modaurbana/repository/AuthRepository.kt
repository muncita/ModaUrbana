package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.models.RegisterRequest
import com.example.modaurbana.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val session: SessionManager) {
    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): LoginResponse =
        withContext(Dispatchers.IO) {
            val resp = api.login(LoginRequest(email = email, password = password))
            resp.authToken?.let { session.saveAuthToken(it) }
            resp
        }

    suspend fun register(name: String, email: String, password: String): LoginResponse =
        withContext(Dispatchers.IO) {
            val resp = api.register(RegisterRequest(name = name, email = email, password = password))
            resp.authToken?.let { session.saveAuthToken(it) }
            resp
        }

    suspend fun currentUser(): UserResponse =
        withContext(Dispatchers.IO) {
            val token = session.getAuthToken() ?: error("No hay token guardado")
            api.getCurrentUser("Bearer $token")
        }
    suspend fun logout() = withContext(Dispatchers.IO) {
        session.clearSession()
    }
}