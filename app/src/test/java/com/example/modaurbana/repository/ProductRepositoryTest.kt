package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.ProductDto
import com.example.modaurbana.models.ApiResponse
import com.example.modaurbana.models.Producto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var repository: ProductRepository
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        sessionManager = mockk(relaxed = true)
        apiService = mockk()
        mockkObject(RetrofitClient)
        every { RetrofitClient.instance } returns apiService
        repository = ProductRepository(sessionManager)
    }
    @After
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun `getProductos devuelve lista mapeada cuando API responde success`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"
        val dto: ProductDto = mockk(relaxed = true) {
            every { id } returns "p1"
            every { nombre } returns "Hoodie Oversize"
            every { precio } returns 24_990.0
            every { estilo } returns "Streetwear"
        }

        val apiResponse = ApiResponse(
            success = true,
            data = listOf(dto),
            message = null
        )

        coEvery {
            apiService.getProductos("Bearer jwt-token")
        } returns apiResponse

        val productos: List<Producto> = repository.getProductos()

        assertEquals(1, productos.size)
        val p = productos.first()
        assertEquals("p1", p.id)
        assertEquals("Hoodie Oversize", p.nombre)
        assertEquals(24_990.0, p.precio!!, 0.0)
    }

    @Test
    fun `getProductos con success false lanza IllegalStateException`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"

        val apiResponse = ApiResponse<List<ProductDto>>(
            success = false,
            data = null,
            message = "Error desde backend"
        )

        coEvery {
            apiService.getProductos("Bearer jwt-token")
        } returns apiResponse

        try {
            repository.getProductos()
            fail("Se esperaba IllegalStateException")
        } catch (e: IllegalStateException) {
            assertTrue(e.message != null && e.message!!.isNotBlank())
        }
    }

    @Test
    fun `getProductos con data nula retorna lista vacia`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"

        val apiResponse = ApiResponse<List<ProductDto>>(
            success = true,
            data = null,
            message = null
        )

        coEvery {
            apiService.getProductos("Bearer jwt-token")
        } returns apiResponse

        val productos = repository.getProductos()

        assertTrue(productos.isEmpty())
    }

    @Test
    fun `getProductoPorId devuelve producto cuando API responde success`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"

        val dto: ProductDto = mockk(relaxed = true) {
            every { id } returns "p42"
            every { nombre } returns "Zapatilla Blanca"
            every { precio } returns 39_990.0
        }

        val apiResponse = ApiResponse(
            success = true,
            data = dto,
            message = null
        )

        coEvery {
            apiService.getProductoPorId("Bearer jwt-token", "p42")
        } returns apiResponse

        val producto: Producto = repository.getProductoPorId("p42")

        assertEquals("p42", producto.id)
        assertEquals("Zapatilla Blanca", producto.nombre)
        assertEquals(39_990.0, producto.precio!!, 0.0)
    }

    @Test
    fun `getProductoPorId con success false lanza IllegalStateException`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"

        val apiResponse = ApiResponse<ProductDto>(
            success = false,
            data = null,
            message = "Producto no encontrado"
        )

        coEvery {
            apiService.getProductoPorId("Bearer jwt-token", "no-existe")
        } returns apiResponse

        try {
            repository.getProductoPorId("no-existe")
            fail("Se esperaba IllegalStateException")
        } catch (e: IllegalStateException) {
            assertTrue(e.message != null && e.message!!.isNotBlank())
        }
    }
}
