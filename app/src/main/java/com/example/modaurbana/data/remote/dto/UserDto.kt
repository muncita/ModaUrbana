package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO = estructura tal como viene del backend (Nest/Mongo)
 */
data class UserDto(
    // Mongo usa "_id"
    @SerializedName("_id")
    val id: String?,

    @SerializedName("email")
    val email: String,

    // Rol del usuario (ADMIN, CLIENTE, PRODUCTOR, etc.)
    @SerializedName("role")
    val role: String? = null,

    // URL del avatar si existe
    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null,

    @SerializedName("emailVerified")
    val emailVerified: Boolean? = null
)
