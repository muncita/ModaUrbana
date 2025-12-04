package com.example.modaurbana.repository

import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.data.remote.dto.LoginData
import com.example.modaurbana.data.remote.dto.LoginRequest
import com.example.modaurbana.data.remote.dto.LoginResponse
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.models.ApiResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        sessionManager = mockk(relaxed = true)
        apiService = mockk()
        mockkObject(RetrofitClient)
        every { RetrofitClient.instance } returns apiService
        repository = AuthRepository(sessionManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login exitoso guarda token y devuelve usuario mapeado`() = runTest {
        val email = "test@correo.cl"
        val password = "12345678"
        val token = "jwt-mock-token"
        val userDto = UserResponseDto(
            id = "abc123",
            email = email,
            name = "Tester",
            role = "CLIENTE"
        )
        val loginData = LoginData(
            accessToken = token,
            user = userDto
        )
        val response = LoginResponse(
            success = true,
            message = null,
            data = loginData
        )
        coEvery {
            apiService.login(LoginRequest(email = email, password = password))
        } returns response
        val user = repository.login(email, password)
        assertEquals(email, user.email)
        assertEquals("Tester", user.name)
        assertEquals("CLIENTE", user.role)
        coVerify { sessionManager.saveAuthToken(token) }
    }

    @Test
    fun `login con success false lanza excepcion con mensaje del backend`() = runTest {
        val email = "fail@correo.cl"
        val password = "wrong"
        val response = LoginResponse(
            success = false,
            message = "Credenciales inválidas",
            data = null
        )

        coEvery {
            apiService.login(LoginRequest(email = email, password = password))
        } returns response

        try {
            repository.login(email, password)
            fail("Se esperaba una excepción")
        } catch (e: IllegalStateException) {
            assertTrue(e.message!!.contains("Credenciales inválidas"))
        }
    }

    @Test
    fun `register exitoso guarda token y devuelve usuario`() = runTest {
        val nombre = "Nuevo User"
        val email = "nuevo@correo.cl"
        val password = "12345678"
        val token = "jwt-token-register"
        val userDto = UserResponseDto(
            id = "id-register",
            email = email,
            name = nombre,
            role = "CLIENTE"
        )
        val loginData = LoginData(
            accessToken = token,
            user = userDto
        )
        val response = LoginResponse(
            success = true,
            message = null,
            data = loginData
        )
        coEvery { apiService.register(any()) } returns response

        val user = repository.register(nombre, email, password)

        assertEquals(email, user.email)
        assertEquals(nombre, user.name)
        coVerify { sessionManager.saveAuthToken(token) }
    }
    @Test
    fun `currentUser usa token guardado y devuelve usuario`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns "jwt-token"
        val userDto = UserResponseDto(
            id = "u1",
            email = "profile@correo.cl",
            name = "Profile User",
            role = "CLIENTE"
        )
        val apiResponse = ApiResponse(
            success = true,
            data = userDto,
            message = null
        )
        coEvery { apiService.getCurrentUser("Bearer jwt-token") } returns apiResponse
        val user = repository.currentUser()
        assertEquals("profile@correo.cl", user.email)
        assertEquals("Profile User", user.name)
        assertEquals("CLIENTE", user.role)
    }
    @Test
    fun `currentUser sin token guardado lanza excepcion`() = runTest {
        coEvery { sessionManager.getAuthToken() } returns null
        try {
            repository.currentUser()
            fail("Se esperaba una excepción por falta de token")
        } catch (e: IllegalStateException) {
            assertTrue(e.message != null && e.message!!.isNotBlank())
        }
    }
    @Test
    fun `logout limpia la sesion en SessionManager`() = runTest {
        repository.logout()
        coVerify { sessionManager.clearSession() }
    }
}
