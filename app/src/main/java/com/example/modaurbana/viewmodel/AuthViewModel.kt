package com.example.modaurbana.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

/**
 * Stub mínimo para compilar y probar pantallas.
 * Luego lo conectamos a tu repositorio/ApiService (login y /me).
 */
class AuthViewModel : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    fun login(username: String, password: String) {
        // TODO: Reemplazar por repo.login(...) y manejo real de errores
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            // Simulación breve de red
            delay(600)
            if (username.isBlank() || password.length < 4) {
                _ui.value = AuthUiState(error = "Credenciales inválidas")
            } else {
                _ui.value = AuthUiState(success = true)
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        // TODO: Reemplazar por repo.register(...) si tu API lo soporta
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            delay(600)
            if (username.isBlank() || !email.contains("@") || password.length < 6) {
                _ui.value = AuthUiState(error = "Revisa los campos del formulario")
            } else {
                _ui.value = AuthUiState(success = true)
            }
        }
    }
}
