package com.example.modaurbana.data.remote.dto

import com.example.modaurbana.models.Producto
import com.google.gson.annotations.SerializedName

/**
 * DTO que mapea EXACTAMENTE lo que devuelve el microservicio de producto (NestJS + Mongo).
 *
 * Importante:
 *  - Mongoose expone el ID como "_id"
 *  - El resto de campos respetan los nombres del schema: nombre, descripcion, talla, material, estilo, precio, imagen, imagenThumbnail, etc.
 */
data class ProductDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("talla") val talla: String? = null,
    @SerializedName("material") val material: String? = null,
    @SerializedName("estilo") val estilo: String? = null,
    @SerializedName("precio") val precio: Double? = null,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("imagenThumbnail") val imagenThumbnail: String? = null
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
    imagen = imagen,
    imagenThumbnail = imagenThumbnail
)

