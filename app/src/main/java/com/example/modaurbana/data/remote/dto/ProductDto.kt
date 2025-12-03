package com.example.modaurbana.data.remote.dto

import com.example.modaurbana.models.Producto
import com.google.gson.annotations.SerializedName


data class ProductDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("talla") val talla: String? = null,
    @SerializedName("material") val material: String? = null,
    @SerializedName("estilo") val estilo: String? = null,
    @SerializedName("precio") val precio: Double? = null,

    // 👇 nuevos campos que también puede devolver la API
    @SerializedName("categoria") val categoria: CategoriaDto? = null,
    @SerializedName("tendencia") val tendencia: Boolean? = null,
    @SerializedName("color") val color: String? = null,

    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("imagenThumbnail") val imagenThumbnail: String? = null
)

data class CategoriaDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("nombre") val nombre: String? = null
)
/**
 * Mapeo DTO -> dominio
 */
fun ProductDto.toDomain(): Producto = Producto(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    talla = talla,
    material = material,
    estilo = estilo,
    precio = precio,
    categoria = categoria?.id,
    tendencia = tendencia,
    color = color,
    imagen = imagen,
    imagenThumbnail = imagenThumbnail
)
