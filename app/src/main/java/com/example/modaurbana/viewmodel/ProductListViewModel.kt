package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.models.Producto
import com.example.modaurbana.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    app: Application,
    // 🔹 inyectable para tests, con default para producción
    private val repo: ProductRepository = ProductRepository(
        SessionManager(app.applicationContext)
    )
) : AndroidViewModel(app) {

    private val _ui = MutableStateFlow(ProductListUiState())
    val ui: StateFlow<ProductListUiState> = _ui

    init {
        loadProductos()
    }

    fun loadProductos() {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(
                    isLoading = true,
                    error = null
                )

                val productos = repo.getProductos()

                val tipos = productos
                    .mapNotNull { mapCategoriaToTipo(it.categoria) }
                    .distinct()

                val estilos = productos
                    .mapNotNull { it.estilo }
                    .distinct()

                _ui.value = _ui.value.copy(
                    isLoading = false,
                    productos = productos,
                    productosFiltrados = productos,
                    tiposDisponibles = tipos,
                    estilosDisponibles = estilos
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar productos"
                )
            }
        }
    }

    private fun mapCategoriaToTipo(categoriaId: String?): String? {
        return when (categoriaId) {
            "673000000000000000000001" -> "Hoodies"
            "673000000000000000000002" -> "Poleras"
            "673000000000000000000003" -> "Pantalones"
            "673000000000000000000004" -> "Zapatillas"
            "673000000000000000000005" -> "Accesorios"
            else -> null
        }
    }

    fun aplicarFiltros(
        tipoPrenda: String?,
        estilo: String?
    ) {
        val productos = _ui.value.productos

        val filtrados = productos.filter { p ->
            val tipo = mapCategoriaToTipo(p.categoria)

            (tipoPrenda == null || tipo == tipoPrenda) &&
                    (estilo == null || p.estilo == estilo)
        }

        _ui.value = _ui.value.copy(
            tipoSeleccionado = tipoPrenda,
            estiloSeleccionado = estilo,
            productosFiltrados = filtrados
        )
    }
}


/**
 * UI State del catálogo.
 * Ya no usamos talla/material, solo:
 * - tipo de prenda
 * - estilo
 */
data class ProductListUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val productosFiltrados: List<Producto> = emptyList(),
    val error: String? = null,

    // Tipos de prenda (Hoodies, Poleras, etc.)
    val tiposDisponibles: List<String> = emptyList(),
    val tipoSeleccionado: String? = null,

    // Estilos (Streetwear, Minimalista, etc.)
    val estilosDisponibles: List<String> = emptyList(),
    val estiloSeleccionado: String? = null
)
