package com.example.modaurbana.models

import com.google.gson.annotations.SerializedName

// ---------------------------------------------------------

data class RegisterRequest(

    @SerializedName("nombre")
    val nombre: String,

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


// --------------------------------------------------------
data class UserResponse(
    val id: String?,
    val name: String?,
    val email: String?,
    val role: String?,
    val accountId: Int? = null,
    val createdAt: String?
)


// ---------------------------------------------------------
data class Producto(

    @SerializedName("_id")
    val id: String?,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("talla")
    val talla: String? = null,

    @SerializedName("material")
    val material: String? = null,

    @SerializedName("estilo")
    val estilo: String? = null,

    @SerializedName("precio")
    val precio: Double? = null,


    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("tendencia")
    val tendencia: Boolean? = null,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("imagen")
    val imagen: String? = null,

    @SerializedName("imagenThumbnail")
    val imagenThumbnail: String? = null
)

// ---------------------------------------------------------
data class CartItem(
    val producto: Producto,
    val quantity: Int
)


// ---------------------------------------------------------
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String? = null
)
