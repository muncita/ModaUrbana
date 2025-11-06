package com.example.modaurbana.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

/**
 * Manejo de estados de carga/éxito/error (Guía 12) y validaciones (Guía 11).
 * No pegamos lógicas completas para evaluación: coloca tus reglas en los TODO.
 */
class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    fun login(username: String, password: String) {
        // TODO: Validaciones campo a campo (Guía 11): requerido, formato, mensajes específicos.
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            val r = repo.login(username, password)
            _ui.value = r.fold(
                onSuccess = { AuthUiState(success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Error de autenticación") }
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        // TODO: Validaciones con reglas de Guía 11 (correo válido, contraseña, etc.)
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            val r = repo.register(username, password, email)
            _ui.value = r.fold(
                onSuccess = { AuthUiState(success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Error al registrar") }
            )
        }
    }
}
