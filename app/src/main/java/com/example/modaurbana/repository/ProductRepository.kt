package com.example.modaurbana.repository


import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.ProductDto
import com.example.modaurbana.data.remote.dto.toDomain
import com.example.modaurbana.models.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {

    private val api = RetrofitClient.instance

    /**
     * Obtiene la lista de productos desde el microservicio Nest.
     */
    suspend fun getProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val resp = api.getProductos()

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
        val resp = api.getProductoPorId(id)

        if (!resp.success || resp.data == null) {
            throw IllegalStateException(resp.message ?: "Producto no encontrado")
        }

        resp.data.toDomain()
    }
}
