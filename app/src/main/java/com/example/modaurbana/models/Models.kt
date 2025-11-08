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

data class TokenResponse(
    @SerializedName("authToken") val authToken: String?,
    @SerializedName("user_id") val userId: String?
) {
    val resolvedToken: String
        get() = authToken ?: ""
}

data class User(
    val id: Long,
    val name: String?,
    val email: String?
)
