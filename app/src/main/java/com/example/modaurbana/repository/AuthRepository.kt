package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.UserDto

/**
 * Repositorio de autenticación según Guía API REST y EP3:
 * - Login -> guarda token en DataStore (persistencia de sesión).
 * - /me -> obtiene perfil autenticado.
 * - (Opcional) register -> si tu API lo soporta.
 *
 * Referencias: Guía API REST (Retrofit+DataStore), EP3 (consumo de Backend, /me).
 */
class AuthRepository(
    private val api: ApiService,
    private val session: SessionManager
) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val res = api.login(LoginRequest(username, password)) // TODO: confirmar firma en tu ApiService
            session.saveAuthToken(res.accessToken) // Persistencia: mantiene sesión (Guía API REST)
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun me(): Result<UserDto> {
        return try {
            Result.success(api.getCurrentUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String, email: String): Result<UserDto> {
        // TODO: Solo si tu API tiene /register. Si no, deja este método sin uso.
        return try {
            // val user = api.register(RegisterRequest(...))
            // Result.success(user)
            Result.failure(UnsupportedOperationException("Implementa /register si tu API lo soporta"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
