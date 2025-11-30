package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.toDomain
import com.example.modaurbana.models.RegisterRequest
import com.example.modaurbana.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val session: SessionManager) {

    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): UserResponse =
        withContext(Dispatchers.IO) {
            val resp = api.login(LoginRequest(email = email, password = password))

            val token = resp.data?.accessToken
                ?: error("Login sin access_token en la respuesta")

            session.saveAuthToken(token)

            val userDto = resp.data.user
                ?: error("Login sin objeto user en la respuesta")

            userDto.toDomain()
        }

    suspend fun register(name: String, email: String, password: String): UserResponse =
        withContext(Dispatchers.IO) {
            val resp = api.register(RegisterRequest(name = name, email = email, password = password))

            val token = resp.data?.accessToken
                ?: error("Registro sin access_token en la respuesta")

            session.saveAuthToken(token)

            val userDto = resp.data.user
                ?: error("Registro sin objeto user en la respuesta")

            userDto.toDomain()
        }

    suspend fun currentUser(): UserResponse =
        withContext(Dispatchers.IO) {
            val token = session.getAuthToken() ?: error("No hay token guardado")
            val wrapper = api.getCurrentUser("Bearer $token")
            val dto = wrapper.data ?: error("Respuesta de perfil sin data")
            dto.toDomain()
        }

    suspend fun logout() = withContext(Dispatchers.IO) {
        session.clearSession()
    }
}
