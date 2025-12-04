package com.example.modaurbana.viewmodel

import com.example.modaurbana.models.Producto
import org.junit.Assert.assertEquals
import org.junit.Test

class CartViewModelTest {

    // Helper para crear productos de prueba
    private fun fakeProducto(
        id: String = "prod-1",
        nombre: String = "Producto 1",
        precio: Double = 10000.0
    ): Producto {
        return Producto(
            id = id,
            nombre = nombre,
            precio = precio
        )
    }

    @Test
    fun `addToCart agrega producto nuevo con cantidad 1 y total correcto`() {
        // GIVEN
        val vm = CartViewModel()
        val producto = fakeProducto(id = "prod-1", precio = 10000.0)

        // WHEN
        vm.addToCart(producto)

        // THEN
        val ui = vm.ui.value

        assertEquals(1, ui.items.size)               // hay 1 item
        assertEquals(1, ui.items[0].quantity)        // cantidad = 1
        assertEquals(10000.0, ui.total, 0.001)  // total = 10000
    }

    @Test
    fun `addToCart incrementa cantidad si el producto ya existe`() {

        val vm = CartViewModel()
        val producto = fakeProducto(id = "prod-1", precio = 5000.0)


        vm.addToCart(producto)
        vm.addToCart(producto)

        val ui = vm.ui.value

        assertEquals(1, ui.items.size)
        assertEquals(2, ui.items[0].quantity)
        assertEquals(10000.0, ui.total, 0.001)
    }

    @Test
    fun `removeOne disminuye cantidad y elimina el item cuando llega a 0`() {

        val vm = CartViewModel()
        val producto = fakeProducto(id = "prod-1", precio = 5000.0)

        vm.addToCart(producto)
        vm.addToCart(producto)
        vm.removeOne("prod-1")
        var ui = vm.ui.value

        assertEquals(1, ui.items.size)
        assertEquals(1, ui.items[0].quantity)
        assertEquals(5000.0, ui.total, 0.001)

        vm.removeOne("prod-1")
        ui = vm.ui.value

        assertEquals(0, ui.items.size)
        assertEquals(0.0, ui.total, 0.001)
    }

    @Test
    fun `clearCart limpia todos los items y total`() {
        val vm = CartViewModel()
        val p1 = fakeProducto(id = "prod-1", precio = 5000.0)
        val p2 = fakeProducto(id = "prod-2", nombre = "Producto 2", precio = 7000.0)
        vm.addToCart(p1)
        vm.addToCart(p2)
        vm.addToCart(p2)
        vm.clearCart()
        val ui = vm.ui.value
        assertEquals(0, ui.items.size)
        assertEquals(0.0, ui.total, 0.001)
    }
}

