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
 * Estado de la UI para el login.
 * Controla carga, errores y éxito.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

/**
 * ViewModel que maneja la autenticación del usuario.
 * Se comunica con AuthRepository → ApiService (Xano).
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        RetrofitClient.create(application).create(ApiService::class.java),
        SessionManager(application)
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Ejecuta el login real contra la API de Xano.
     * Guarda el token JWT en SessionManager si el login es exitoso.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(username, password)
            _uiState.value = result.fold(
                onSuccess = {
                    LoginUiState(success = true)
                },
                onFailure = { e ->
                    LoginUiState(
                        error = e.localizedMessage ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    /**
     * Limpia el estado (por ejemplo, cuando el usuario cambia los campos del formulario)
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
