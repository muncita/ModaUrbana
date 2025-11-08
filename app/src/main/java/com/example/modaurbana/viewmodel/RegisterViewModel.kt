package com.example.modaurbana.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI del registro de usuario.
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel encargado de manejar el registro de usuarios.
 * Llama al AuthRepository → /auth/signup en la API Xano.
 */
class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val repository = AuthRepository(session)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    /**
     * Ejecuta el registro real contra la API.
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)

            try {
                repository.register(name, email, password)
                _uiState.value = RegisterUiState(success = true)
            } catch (e: Exception) {
                _uiState.value = RegisterUiState(
                    error = e.localizedMessage ?: "Error al registrar usuario"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}
