package com.example.modaurbana.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("authToken") val authToken: String?,
    // ajusta al campo real del backend: "user_id", "userId", etc.
    @SerializedName("user_id") val userId: String?
)

data class UserResponse(
    val id: Int?,
    @SerializedName("created_at") val createdAt: String?,
    val name: String?,
    val email: String?,
    @SerializedName("account_id") val accountId: Int?,
    val role: String?
)
