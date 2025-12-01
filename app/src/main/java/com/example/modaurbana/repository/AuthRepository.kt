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

    // ---------------------------------------------------------
    //                       LOGIN
    // ---------------------------------------------------------
    suspend fun login(email: String, password: String): UserResponse =
        withContext(Dispatchers.IO) {

            val body = LoginRequest(email = email, password = password)
            val resp = api.login(body)

            if (!resp.success) {
                error(resp.message ?: "Error al iniciar sesión")
            }

            val data = resp.data ?: error("Respuesta de login sin data")
            val token = data.accessToken ?: error("Respuesta de login sin token")
            val userDto = data.user ?: error("Respuesta de login sin usuario")

            // Guardar token JWT
            session.saveAuthToken(token)

            // Mapear al modelo de dominio de la app
            userDto.toDomain()
        }


    // ---------------------------------------------------------
    //                     REGISTER
    // ---------------------------------------------------------
    suspend fun register(nombre: String, email: String, password: String): UserResponse =
        withContext(Dispatchers.IO) {

            val body = RegisterRequest(
                nombre = nombre,
                email = email,
                password = password,
                role = "CLIENTE"  // por defecto todo usuario nuevo es CLIENTE
            )

            val resp = api.register(body)

            if (!resp.success) {
                error(resp.message ?: "Error al registrar usuario")
            }

            val data = resp.data ?: error("Respuesta de registro sin data")
            val token = data.accessToken ?: error("Respuesta de registro sin token")
            val userDto = data.user ?: error("Respuesta de registro sin usuario")

            // Guardar token JWT
            session.saveAuthToken(token)

            // Mapear al modelo de dominio
            userDto.toDomain()
        }


    // ---------------------------------------------------------
    //                 PERFIL DEL USUARIO
    // ---------------------------------------------------------
    suspend fun currentUser(): UserResponse =
        withContext(Dispatchers.IO) {

            val token = session.getAuthToken() ?: error("No hay token guardado")

            val wrapper = api.getCurrentUser("Bearer $token")

            if (!wrapper.success) {
                error(wrapper.message ?: "Error al obtener el perfil del usuario")
            }

            val dto = wrapper.data ?: error("Respuesta de perfil sin data")

            dto.toDomain()
        }


    // ---------------------------------------------------------
    //                      LOGOUT
    // ---------------------------------------------------------
    suspend fun logout() = withContext(Dispatchers.IO) {
        session.clearSession()
    }
}
