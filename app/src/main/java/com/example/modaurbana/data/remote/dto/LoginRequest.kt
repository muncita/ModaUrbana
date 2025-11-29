package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la petición de login.
 * Adaptado a tu backend actual (email + password).
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
