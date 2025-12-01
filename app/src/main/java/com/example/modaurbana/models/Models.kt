package com.example.modaurbana.models

import com.google.gson.annotations.SerializedName

// ---------------------------------------------------------
// REGISTER REQUEST — Debe alinearse EXACTO con RegisterDto Nest
// ---------------------------------------------------------
data class RegisterRequest(

    @SerializedName("nombre")
    val nombre: String,                 // Nest pide "nombre", no "name"

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    // Por defecto, todo usuario registrado es CLIENTE
    @SerializedName("role")
    val role: String = "CLIENTE",

    @SerializedName("telefono")
    val telefono: String? = null,

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("tallas")
    val tallas: String? = null,

    @SerializedName("preferencias")
    val preferencias: List<String>? = null
)


// ---------------------------------------------------------
// USER RESPONSE — Lo que devuelve login/register/profile
// ---------------------------------------------------------
data class UserResponse(
    val id: String?,
    val name: String?,
    val email: String?,
    val role: String?,
    val accountId: Int? = null,   // tu backend no usa esto pero lo mantenemos opcional
    val createdAt: String?
)


// ---------------------------------------------------------
// PRODUCTO (MODELO DE DOMINIO USADO EN TU APP)
// ---------------------------------------------------------
data class Producto(
    val id: String?,               // ID Mongo
    val nombre: String,            // Nombre visible
    val descripcion: String?,      // Descripción
    val talla: String?,            // XS/S/M/L etc
    val material: String?,         // Algodón / Cuero / Mezclilla
    val estilo: String?,           // Streetwear / Casual
    val precio: Double?,           // Precio
    val imagen: String?,           // Imagen principal
    val imagenThumbnail: String?   // Thumbnail
)


// ---------------------------------------------------------
// ITEM DEL CARRITO
// ---------------------------------------------------------
data class CartItem(
    val producto: Producto,
    val quantity: Int
)


// ---------------------------------------------------------
// API RESPONSE — Wrapper genérico del backend NestJS
// ---------------------------------------------------------
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String? = null
)
