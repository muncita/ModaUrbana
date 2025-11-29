package com.example.modaurbana.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)


data class UserResponse(
    val id: Int?,
    @SerializedName("created_at") val createdAt: String?,
    val name: String?,
    val email: String?,
    @SerializedName("account_id") val accountId: Int?,
    val role: String?
)

data class Producto(
    val id: String?,           // ID del producto en Mongo/Nest
    val nombre: String,        // Nombre visible en catálogo
    val descripcion: String?,  // Descripción opcional
    val talla: String?,        // XS, S, M, L, XL, etc.
    val material: String?,     // Algodón, cuero, mezclilla, etc.
    val estilo: String?,       // Urbano, casual, deportivo, etc.
    val precio: Double?,       // Precio de referencia (si lo manejas)
    val imagen: String?,       // URL imagen principal
    val imagenThumbnail: String? // URL thumbnail
)

data class CartItem(
    val producto: Producto,
    val quantity: Int
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
