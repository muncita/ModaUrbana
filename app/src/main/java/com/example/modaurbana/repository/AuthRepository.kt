package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.models.LoginResponse
import com.example.modaurbana.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AuthRepository
 * Maneja autenticación, registro y sesión persistente con Xano.
 */
class AuthRepository(private val session: SessionManager) {

    private val api = RetrofitClient.instance

    /**
     * Inicia sesión con email y contraseña.
     * Guarda el token recibido en SessionManager.
     */
    suspend fun login(email: String, password: String): LoginResponse = withContext(Dispatchers.IO) {
        val response = api.login(mapOf("email" to email, "password" to password))

        // Guardar el token si la API lo devuelve correctamente
        response.authToken?.let { session.saveAuthToken(it) }

        return@withContext response
    }

    /**
     * Registra un nuevo usuario en Xano.
     * También guarda el token para mantener la sesión activa.
     */
    suspend fun register(name: String, email: String, password: String): LoginResponse = withContext(Dispatchers.IO) {
        val response = api.register(mapOf("name" to name, "email" to email, "password" to password))

        // Guardar token si el registro fue exitoso
        response.authToken?.let { session.saveAuthToken(it) }

        return@withContext response
    }

    /**
     * Obtiene el usuario autenticado desde el endpoint /auth/me
     */
    suspend fun currentUser(): UserResponse = withContext(Dispatchers.IO) {
        val token = session.getAuthToken()
        require(!token.isNullOrEmpty()) { "No hay token guardado" }

        return@withContext api.getCurrentUser("Bearer $token")
    }

    /**
     * Cierra sesión limpiando el token.
     */
    suspend fun logout() = withContext(Dispatchers.IO) {
        session.clearAuthToken()
    }
}
