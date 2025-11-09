package com.example.modaurbana.models

/**
 * Modelos de datos para comunicar la app con la API de Xano.
 * - Define la estructura de las peticiones (Request)
 * - Define la estructura de las respuestas (Response)
 */

// 🔹 Petición de inicio de sesión
data class LoginRequest(
    val email: String,
    val password: String
)

// 🔹 Petición de registro
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// ✅ Unificamos nombre con el ViewModel y el Repository
// 🔹 Respuesta al registrarse o iniciar sesión
data class LoginResponse(
    val authToken: String?,
    val user_id: String?
)

// 🔹 Datos del usuario autenticado (GET /auth/me)
data class UserResponse(
    val id: Int?,
    val created_at: String?,
    val name: String?,
    val email: String?,
    val account_id: Int?,
    val role: String?
)
