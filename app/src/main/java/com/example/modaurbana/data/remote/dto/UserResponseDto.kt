package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO crudo tal como lo devuelve Xano o el backend Nest.
 * Esto NO se usa directamente en la UI.
 * El repositorio lo convertirá a UserResponse (model).
 */
data class UserResponseDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("account_id")
    val accountId: Int?,

    @SerializedName("role")
    val role: String?
)
