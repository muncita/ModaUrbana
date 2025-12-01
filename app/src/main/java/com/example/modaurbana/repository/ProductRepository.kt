package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.ProductDto
import com.example.modaurbana.data.remote.dto.toDomain
import com.example.modaurbana.models.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val session: SessionManager
) {

    private val api = RetrofitClient.instance

    /**
     * Obtiene la lista de productos desde el microservicio Nest.
     */
    suspend fun getProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val token = session.getAuthToken() ?: error("No hay token guardado (usuario no logueado)")

        val resp = api.getProductos("Bearer $token")

        if (!resp.success) {
            throw IllegalStateException(resp.message ?: "Error desconocido al obtener productos")
        }

        val dtoList: List<ProductDto> = resp.data ?: emptyList()
        dtoList.map { it.toDomain() }
    }

    /**
     * Obtiene el detalle de un producto por su ID.
     */
    suspend fun getProductoPorId(id: String): Producto = withContext(Dispatchers.IO) {
        val token = session.getAuthToken() ?: error("No hay token guardado (usuario no logueado)")

        val resp = api.getProductoPorId("Bearer $token", id)

        if (!resp.success || resp.data == null) {
            throw IllegalStateException(resp.message ?: "Producto no encontrado")
        }

        resp.data.toDomain()
    }
}
