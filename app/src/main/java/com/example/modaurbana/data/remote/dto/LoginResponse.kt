package com.example.modaurbana.data.remote.dto

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val data: LoginData?
)

data class LoginData(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("user")
    val user: UserResponseDto?
)
