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
 * ViewModel de autenticación.
 *
 * Constructor PRINCIPAL (para tests):
 *   - Recibe AuthRepository y SessionManager inyectados.
 *
 * Constructor SECUNDARIO (para la app en runtime):
 *   - Crea SessionManager real y AuthRepository real.
 */
class AuthViewModel(
    app: Application,
    private val repo: AuthRepository,
    private val session: SessionManager
) : AndroidViewModel(app) {

    /**
     * Constructor secundario usado por la APP en runtime.
     * Aquí sí se crean el SessionManager real y el AuthRepository real.
     */
    constructor(app: Application) : this(
        app,
        AuthRepository(SessionManager(app.applicationContext)),
        SessionManager(app.applicationContext)
    )

    // ----------------- UI STATE -----------------
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    // 🔹 Soporte de avatar guardado en DataStore
    val avatarUriFlow = session.avatarUriFlow

    suspend fun saveAvatarUri(uri: String) {
        session.saveAvatarUri(uri)
    }

    // ----------------- ACCIONES -----------------
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                val user = repo.login(email, password)

                _ui.value = AuthUiState(user = user, isLoading = false)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.message ?: "Error desconocido")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                val user = repo.register(name, email, password)

                _ui.value = AuthUiState(user = user, isLoading = false)
            } catch (e: Exception) {
                _ui.value = AuthUiState(error = e.message ?: "Error desconocido")
            }
        }
    }

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

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _ui.value = AuthUiState()
        }
    }
}

// ----------------- UI STATE -----------------
data class AuthUiState(
    val user: UserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
