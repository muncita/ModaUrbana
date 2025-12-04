package com.example.modaurbana

import android.app.Application
import app.cash.turbine.test
import com.example.modaurbana.models.UserResponse
import com.example.modaurbana.repository.AuthRepository
import com.example.modaurbana.viewmodel.AuthViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private lateinit var repo: AuthRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)

        val app = mockk<Application>(relaxed = true)
        viewModel = AuthViewModel(app)

        // 👉 Inyectamos el repo mock dentro del ViewModel usando reflexión
        repo = mockk(relaxed = true)
        val repoField = AuthViewModel::class.java.getDeclaredField("repo")
        repoField.isAccessible = true
        repoField.set(viewModel, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ---------------------------------------------------------------------
    // LOGIN ÉXITOSO
    // ---------------------------------------------------------------------
    @Test
    fun `login exitoso actualiza uiState con usuario`() = runTest {
        val fakeUser = UserResponse(
            id = "100",
            name = "Pepita",
            email = "pepita@test.com",
            role = "CLIENTE",
            accountId = null,
            createdAt = "2024-01-01"
        )

        coEvery { repo.login("pepita@test.com", "1234") } returns fakeUser

        viewModel.ui.test {
            // 1) Estado inicial
            val initial = awaitItem()
            assertNull(initial.user)
            assertNull(initial.error)

            // 2) Ejecutamos login
            viewModel.login("pepita@test.com", "1234")

            // 3) Siguiente emisión = éxito
            val final = awaitItem()
            assertEquals("Pepita", final.user?.name)
            assertEquals("pepita@test.com", final.user?.email)
            assertNull(final.error)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repo.login("pepita@test.com", "1234") }
    }

    // ---------------------------------------------------------------------
    // LOGIN CON ERROR
    // ---------------------------------------------------------------------
    @Test
    fun `login con error muestra mensaje de error`() = runTest {
        coEvery { repo.login(any(), any()) } throws Exception("Credenciales inválidas")

        viewModel.ui.test {
            val initial = awaitItem()
            assertNull(initial.user)

            viewModel.login("fail@test.com", "badpass")

            val errorState = awaitItem()
            assertEquals("Credenciales inválidas", errorState.error)
            assertNull(errorState.user)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ---------------------------------------------------------------------
    // REGISTER ÉXITOSO
    // ---------------------------------------------------------------------
    @Test
    fun `register exitoso guarda usuario en uiState`() = runTest {
        val newUser = UserResponse(
            id = "200",
            name = "Nuevo User",
            email = "nuevo@test.com",
            role = "CLIENTE",
            accountId = null,
            createdAt = "2024-02-02"
        )

        coEvery { repo.register("Nuevo User", "nuevo@test.com", "abcd") } returns newUser

        viewModel.ui.test {
            val initial = awaitItem()
            assertNull(initial.user)

            viewModel.register("Nuevo User", "nuevo@test.com", "abcd")

            val final = awaitItem()
            assertEquals("Nuevo User", final.user?.name)
            assertEquals("nuevo@test.com", final.user?.email)
            assertNull(final.error)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repo.register("Nuevo User", "nuevo@test.com", "abcd") }
    }

    // ---------------------------------------------------------------------
    // REGISTER CON ERROR
    // ---------------------------------------------------------------------
    @Test
    fun `register con error devuelve mensaje de error`() = runTest {
        coEvery { repo.register(any(), any(), any()) } throws Exception("Email ya registrado")

        viewModel.ui.test {
            val initial = awaitItem()
            assertNull(initial.user)

            viewModel.register("Juan", "juan@test.com", "1234")

            val errorState = awaitItem()
            assertEquals("Email ya registrado", errorState.error)
            assertNull(errorState.user)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
