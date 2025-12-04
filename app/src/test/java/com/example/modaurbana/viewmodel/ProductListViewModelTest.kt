package com.example.modaurbana.viewmodel

import android.app.Application
import com.example.modaurbana.models.Producto
import com.example.modaurbana.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var app: Application
    private lateinit var repo: ProductRepository
    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        app = mockk(relaxed = true)
        repo = mockk()

        viewModel = ProductListViewModel(app, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun fakeProducts(): List<Producto> {
        val hoodie = Producto(
            id = "p1",
            nombre = "Hoodie Oversize",
            descripcion = "Hoodie comfy",
            talla = "L",
            material = "Algodón",
            estilo = "Streetwear",
            precio = 24_990.0,
            categoria = "673000000000000000000001",
            tendencia = true,
            color = "Negro",
            imagen = null,
            imagenThumbnail = null
        )

        val polera = Producto(
            id = "p2",
            nombre = "Polera básica",
            descripcion = "Polera blanca",
            talla = "M",
            material = "Algodón",
            estilo = "Minimalista",
            precio = 9_990.0,
            categoria = "673000000000000000000002",
            tendencia = false,
            color = "Blanco",
            imagen = null,
            imagenThumbnail = null
        )

        val accesorio = Producto(
            id = "p3",
            nombre = "Gorro",
            descripcion = "Gorro lana",
            talla = null,
            material = "Lana",
            estilo = "Streetwear",
            precio = 4_990.0,
            categoria = "673000000000000000000005",
            tendencia = false,
            color = "Gris",
            imagen = null,
            imagenThumbnail = null
        )

        return listOf(hoodie, polera, accesorio)
    }

    @Test
    fun `loadProductos llena ui con productos y filtros cuando repo responde ok`() = runTest {
        val productos = fakeProducts()
        coEvery { repo.getProductos() } returns productos
        viewModel.loadProductos()
        advanceUntilIdle()
        val state = viewModel.ui.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(3, state.productos.size)
        assertEquals(productos, state.productos)
        assertEquals(productos, state.productosFiltrados)
        assertEquals(
            setOf("Hoodies", "Poleras", "Accesorios"),
            state.tiposDisponibles.toSet()
        )
        assertEquals(
            setOf("Streetwear", "Minimalista"),
            state.estilosDisponibles.toSet()
        )
    }

    @Test
    fun `loadProductos pone error cuando repo lanza excepcion`() = runTest {
        coEvery { repo.getProductos() } throws RuntimeException("Fallo red")
        viewModel.loadProductos()
        advanceUntilIdle()
        val state = viewModel.ui.value
        assertFalse(state.isLoading)
        assertEquals("Fallo red", state.error)
        assertTrue(state.productos.isEmpty())
        assertTrue(state.productosFiltrados.isEmpty())
    }

    @Test
    fun `aplicarFiltros filtra lista segun tipo y estilo`() = runTest {
        val productos = fakeProducts()
        coEvery { repo.getProductos() } returns productos
        viewModel.loadProductos()
        advanceUntilIdle()
        viewModel.aplicarFiltros(
            tipoPrenda = "Hoodies",
            estilo = "Streetwear"
        )
        val state = viewModel.ui.value

        assertEquals("Hoodies", state.tipoSeleccionado)
        assertEquals("Streetwear", state.estiloSeleccionado)

        assertEquals(1, state.productosFiltrados.size)
        assertEquals("Hoodie Oversize", state.productosFiltrados.first().nombre)
    }
}
