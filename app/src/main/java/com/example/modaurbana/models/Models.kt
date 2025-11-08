package com.example.modaurbana.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

// Usa un único response para ambos (signup/login)
data class TokenResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("authToken") val authToken: String? = null // por si Xano usa este nombre
) {
    val resolvedToken: String
        get() = token ?: authToken ?: ""
}

data class User(
    val id: Long,
    val name: String?,
    val email: String?
)
