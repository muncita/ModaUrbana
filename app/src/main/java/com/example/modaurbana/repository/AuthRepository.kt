package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val session: SessionManager) {

    private val api = RetrofitClient.api

    suspend fun register(name: String, email: String, pass: String): User {
        val res: TokenResponse = withContext(Dispatchers.IO) {
            api.register(RegisterRequest(name, email, pass))
        }
        val token = res.resolvedToken
        require(token.isNotEmpty()) { "La API no devolvió token en signup" }
        session.saveAuthToken(token)
        return me()
    }

    suspend fun login(email: String, pass: String): User {
        val res: TokenResponse = withContext(Dispatchers.IO) {
            api.login(LoginRequest(email, pass))
        }
        val token = res.resolvedToken
        require(token.isNotEmpty()) { "La API no devolvió token en login" }
        session.saveAuthToken(token)
        return me()
    }

    suspend fun me(): User {
        val token = session.getAuthToken()
        require(token.isNotEmpty()) { "No hay token" }
        return withContext(Dispatchers.IO) {
            api.me("Bearer $token")
        }
    }

    suspend fun logout() {
        session.clearAuthToken()
    }
}
