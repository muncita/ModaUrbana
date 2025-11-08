package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.AuthRepository
import com.example.modaurbana.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para login y registro.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val success: Boolean = false
)

/**
 * ViewModel unificado para autenticación (login, registro, logout y obtener usuario actual).
 */
class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val repository = AuthRepository(session)

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    /**
     * Inicia sesión usando la API de Xano (POST /auth/login)
     */
    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            try {
                val user = repository.login(email, password)
                _ui.value = AuthUiState(user = user, success = true)
            } catch (e: Exception) {
                _ui.value = AuthUiState(
                    error = e.localizedMessage ?: "Error al iniciar sesión"
                )
            }
        }
    }

    /**
     * Registra un nuevo usuario en la API (POST /auth/signup)
     */
    fun doRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            try {
                val user = repository.register(name, email, password)
                _ui.value = AuthUiState(user = user, success = true)
            } catch (e: Exception) {
                _ui.value = AuthUiState(
                    error = e.localizedMessage ?: "Error al registrar usuario"
                )
            }
        }
    }

    /**
     * Obtiene el usuario actual desde /auth/me (si ya hay token guardado)
     */
    fun fetchUser() {
        viewModelScope.launch {
            _ui.value = AuthUiState(isLoading = true)
            try {
                val user = repository.me()
                _ui.value = AuthUiState(user = user)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.localizedMessage)
            }
        }
    }

    /**
     * Cierra la sesión y limpia los datos locales.
     */
    fun doLogout() {
        viewModelScope.launch {
            repository.logout()
            _ui.value = AuthUiState()
        }
    }

    /**
     * Reinicia el estado de la interfaz (por ejemplo, al cambiar de pantalla).
     */
    fun resetState() {
        _ui.value = AuthUiState()
    }
}
