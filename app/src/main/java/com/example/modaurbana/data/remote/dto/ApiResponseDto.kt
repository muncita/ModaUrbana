package com.example.modaurbana.data.remote.dto


import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta genérica del backend NestJS.
 *
 * Ejemplos:
 *  - GET /producto       -> { success: true, data: [...], total: 10 }
 *  - GET /producto/:id   -> { success: true, data: { ... } }
 *  - POST /auth/login    -> { success: true, message: "...", data: { ... } }
 */
data class ApiResponseDto<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String? = null,
    @SerializedName("total") val total: Int? = null
)
