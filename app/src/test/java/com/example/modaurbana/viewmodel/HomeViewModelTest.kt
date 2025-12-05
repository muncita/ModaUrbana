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

        viewModel = HomeViewModel(app, session, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUser con token usa repo y expone nombre`() = runTest {
        coEvery { session.getAuthToken() } returns "jwt-123"
        coEvery { repo.getCurrentUser("jwt-123") } returns fakeUserDto("Monse")
        viewModel.loadUser()
        advanceUntilIdle()
        assertEquals("Monse", viewModel.username.value)
        coVerify(exactly = 1) { repo.getCurrentUser("jwt-123") }
    }

    @Test
    fun `loadUser sin token pone Invitado y no llama repo`() = runTest {
        coEvery { session.getAuthToken() } returns null

        viewModel.loadUser()
        advanceUntilIdle()
        assertEquals("Invitado", viewModel.username.value)
        coVerify(exactly = 0) { repo.getCurrentUser(any()) }
    }

    @Test
    fun `loadUser con error en repo expone mensaje de error`() = runTest {
        coEvery { session.getAuthToken() } returns "jwt-123"
        coEvery { repo.getCurrentUser("jwt-123") } throws RuntimeException("Fallo API")

        viewModel.loadUser()
        advanceUntilIdle()

        assertEquals("Error: Fallo API", viewModel.username.value)
    }

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
