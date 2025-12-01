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

    @SerializedName("_id")
    val id: String?,               // ID Mongo

    @SerializedName("nombre")
    val nombre: String,            // Nombre visible

    @SerializedName("descripcion")
    val descripcion: String? = null,      // Descripción

    @SerializedName("talla")
    val talla: String? = null,            // XS/S/M/L etc

    @SerializedName("material")
    val material: String? = null,         // Algodón / Cuero / Mezclilla

    @SerializedName("estilo")
    val estilo: String? = null,           // Streetwear / Casual

    @SerializedName("precio")
    val precio: Double? = null,           // Precio

    // 👇 NUEVOS CAMPOS CON DEFAULT = null
    @SerializedName("categoria")
    val categoria: String? = null,        // id de la categoría en Mongo

    @SerializedName("tendencia")
    val tendencia: Boolean? = null,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("imagen")
    val imagen: String? = null,           // Imagen principal

    @SerializedName("imagenThumbnail")
    val imagenThumbnail: String? = null   // Thumbnail
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
