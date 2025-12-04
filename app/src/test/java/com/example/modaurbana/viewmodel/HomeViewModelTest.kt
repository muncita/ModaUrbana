package com.example.modaurbana.viewmodel

import android.app.Application
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.dto.UserResponseDto
import com.example.modaurbana.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var app: Application
    private lateinit var session: SessionManager
    private lateinit var repo: UserRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        app = mockk(relaxed = true)
        session = mockk(relaxed = true)
        repo = mockk()

        // Usamos el constructor de pruebas de HomeViewModel
        viewModel = HomeViewModel(app, session, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -----------------------------------------------------------
    // 1) Con token: usa repo y expone nombre del usuario
    // -----------------------------------------------------------
    @Test
    fun `loadUser con token usa repo y expone nombre`() = runTest {
        // GIVEN
        coEvery { session.getAuthToken() } returns "jwt-123"
        coEvery { repo.getCurrentUser("jwt-123") } returns fakeUserDto("Monse")

        // WHEN
        viewModel.loadUser()
        advanceUntilIdle()

        // THEN
        assertEquals("Monse", viewModel.username.value)
        coVerify(exactly = 1) { repo.getCurrentUser("jwt-123") }
    }

    // -----------------------------------------------------------
    // 2) Sin token: pone Invitado y NO llama al repo
    // -----------------------------------------------------------
    @Test
    fun `loadUser sin token pone Invitado y no llama repo`() = runTest {
        // GIVEN
        coEvery { session.getAuthToken() } returns null

        // WHEN
        viewModel.loadUser()
        advanceUntilIdle()

        // THEN
        assertEquals("Invitado", viewModel.username.value)
        coVerify(exactly = 0) { repo.getCurrentUser(any()) }
    }

    // -----------------------------------------------------------
    // 3) Error en repo: expone mensaje de error
    // -----------------------------------------------------------
    @Test
    fun `loadUser con error en repo expone mensaje de error`() = runTest {
        // GIVEN
        coEvery { session.getAuthToken() } returns "jwt-123"
        coEvery { repo.getCurrentUser("jwt-123") } throws RuntimeException("Fallo API")

        // WHEN
        viewModel.loadUser()
        advanceUntilIdle()

        // THEN
        assertEquals("Error: Fallo API", viewModel.username.value)
    }

    // Helper para crear un UserResponseDto falso
    private fun fakeUserDto(nombre: String): UserResponseDto =
        UserResponseDto(
            id = "1",
            email = "test@correo.com",
            name = nombre,
            role = "CLIENTE",
            avatar = null,
            createdAt = "2024-01-01"
        )
}
