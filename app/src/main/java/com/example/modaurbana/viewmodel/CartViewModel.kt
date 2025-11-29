package com.example.modaurbana.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modaurbana.models.CartItem
import com.example.modaurbana.models.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartUiState(
    val items: List<CartItem> = emptyList()
) {
    val total: Double
        get() = items.sumOf { (it.producto.precio ?: 0.0) * it.quantity }
}

class CartViewModel : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui

    fun addToCart(producto: Producto) {
        val current = _ui.value.items.toMutableList()
        val existingIndex = current.indexOfFirst { it.producto.id == producto.id }

        if (existingIndex >= 0) {
            val currentItem = current[existingIndex]
            current[existingIndex] = currentItem.copy(quantity = currentItem.quantity + 1)
        } else {
            current.add(CartItem(producto = producto, quantity = 1))
        }

        _ui.value = CartUiState(items = current)
    }

    fun removeOne(productId: String?) {
        if (productId == null) return
        val current = _ui.value.items.toMutableList()
        val index = current.indexOfFirst { it.producto.id == productId }
        if (index >= 0) {
            val item = current[index]
            if (item.quantity > 1) {
                current[index] = item.copy(quantity = item.quantity - 1)
            } else {
                current.removeAt(index)
            }
            _ui.value = CartUiState(items = current)
        }
    }

    fun clearCart() {
        _ui.value = CartUiState(items = emptyList())
    }
}
