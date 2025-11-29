package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de login.
 * Adaptado al backend actual (Xano): authToken + user_id.
 */
data class LoginResponse(
    @SerializedName("authToken")
    val authToken: String?,

    @SerializedName("user_id")
    val userId: String?
)
