package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.models.UserResponse
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel central para manejar login, registro y sesión persistente.
 */
class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val repo = AuthRepository(session)

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    /**
     * Iniciar sesión
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                // 1️⃣ Login → obtener token
                val loginResponse = repo.login(email, password)

                // 2️⃣ Obtener datos del usuario autenticado
                val user = repo.currentUser()

                // 3️⃣ Actualizar UI
                _ui.value = AuthUiState(user = user, isLoading = false)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.message ?: "Error desconocido")
            }
        }
    }
    val avatarUriFlow = session.avatarUriFlow

    suspend fun saveAvatarUri(uri: String) {
        session.saveAvatarUri(uri)
    }
    /**
     * Registrar nuevo usuario
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                // 1️⃣ Registro → obtener token
                val registerResponse = repo.register(name, email, password)

                // 2️⃣ Obtener usuario autenticado tras registro
                val user = repo.currentUser()

                // 3️⃣ Actualizar UI
                _ui.value = AuthUiState(user = user, isLoading = false)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Cargar usuario actual (cuando la app inicia)
     */
    fun loadUser() {
        viewModelScope.launch {
            try {
                val user = repo.currentUser()
                _ui.value = AuthUiState(user = user)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.message)
            }
        }
    }

    /**
     * Cerrar sesión
     */
    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _ui.value = AuthUiState()
        }
    }
}

/**
 * Estado de la UI (flujo observable)
 */
data class AuthUiState(
    val user: UserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

