package com.example.modaurbana.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI del login/registro.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

/**
 * ViewModel central de autenticación.
 * Controla tanto el login como el registro (dummy o real).
 */
class AuthViewModel : ViewModel() {

    // Inicializa Retrofit y SessionManager
    private val retrofit = RetrofitClient.create()
    private val api = retrofit.create(ApiService::class.java)
    private val sessionManager = SessionManager(RetrofitClient.Context)
    private val repository = AuthRepository(api, sessionManager)

    // Estado privado y público
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    /**
     * Inicia sesión con la API (usa AuthRepository → /auth/login)
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)

            val result = repository.login(username, password)
            _ui.value = result.fold(
                onSuccess = {
                    AuthUiState(success = true)
                },
                onFailure = { e ->
                    AuthUiState(error = e.localizedMessage ?: "Error al iniciar sesión")
                }
            )
        }
    }

    /**
     * Simula o conecta un registro real con Xano.
     * Puedes reemplazar con repository.register() si tienes el endpoint.
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)

            try {
                // ⚙️ Aquí podrías conectar con tu endpoint real de registro
                // val result = repository.register(username, email, password)

                kotlinx.coroutines.delay(1200) // Simulación de red
                _ui.value = AuthUiState(success = true)

            } catch (e: Exception) {
                _ui.value = AuthUiState(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error al registrar usuario"
                )
            }
        }
    }

    /**
     * Limpia el estado de la UI (por ejemplo, al cambiar de pantalla).
     */
    fun resetState() {
        _ui.value = AuthUiState()
    }
}
