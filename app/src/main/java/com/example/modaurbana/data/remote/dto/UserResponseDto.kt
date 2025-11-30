package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.example.modaurbana.models.UserResponse


data class UserResponseDto(
    @SerializedName("_id")
    val id: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("role")
    val role: String?,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)


fun UserResponseDto.toDomain(): UserResponse =
    UserResponse(
        id = null,
        createdAt = createdAt,
        name = name,
        email = email,
        accountId = null,
        role = role
    )
