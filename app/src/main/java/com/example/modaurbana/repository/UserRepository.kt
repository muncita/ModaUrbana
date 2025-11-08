package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.models.User

class UserRepository(
    private val session: SessionManager,
    private val api: ApiService = RetrofitClient.api
) {

    /**
     * Retorna el usuario autenticado usando /auth/me.
     * Requiere que el token esté guardado en DataStore (lo hace AuthRepository al login/signup).
     */
    suspend fun fetchMe(): Result<User> = try {
        val token = session.getAuthToken()
        require(!token.isNullOrEmpty()) { "No hay token de sesión" }
        val me = api.me("Bearer $token")
        Result.success(me)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

