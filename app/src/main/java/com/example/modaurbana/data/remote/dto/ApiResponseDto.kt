package com.example.modaurbana.data.remote.dto


import com.google.gson.annotations.SerializedName


data class ApiResponseDto<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String? = null,
    @SerializedName("total") val total: Int? = null
)
