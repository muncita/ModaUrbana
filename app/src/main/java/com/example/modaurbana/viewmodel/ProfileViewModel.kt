package com.example.modaurbana.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.data.remote.ApiService
import com.example.modaurbana.data.remote.RetrofitClient
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI del perfil de usuario.
 * Contiene datos visibles en ProfileScreen.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userEmail: String = "",
    val error: String? = null,
    val avatarUri: Uri? = null
)

/**
 * ViewModel del perfil de usuario.
 * Se encarga de obtener los datos del usuario autenticado (endpoint /user/me)
 * y manejar la imagen de perfil (galería o cámara) con persistencia local.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio de autenticación
    private val authRepo = AuthRepository(
        RetrofitClient.create(application).create(ApiService::class.java),
        SessionManager(application)
    )

    // Persistencia local (DataStore)
    private val sessionManager = SessionManager(application)

    // Estado interno (privado)
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado expuesto a la UI
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * Carga los datos del usuario autenticado desde la API.
     * Usa /user/me (token ya inyectado por AuthInterceptor).
     */
    fun loadUser() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = authRepo.me()
            _uiState.update { current ->
                result.fold(
                    onSuccess = { user ->
                        current.copy(
                            isLoading = false,
                            userName = user.firstName + " " + user.lastName,
                            userEmail = user.email,
                            error = null
                        )
                    },
                    onFailure = { e ->
                        current.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Error al cargar el perfil"
                        )
                    }
                )
            }

            // Luego de cargar los datos, intenta restaurar el avatar local
            restoreAvatar()
        }
    }

    /**
     * Actualiza la imagen de perfil en memoria y la guarda localmente.
     */
    fun updateAvatar(uri: Uri?) {
        _uiState.update { it.copy(avatarUri = uri) }
        viewModelScope.launch {
            sessionManager.saveAvatarUri(uri?.toString() ?: "")
        }
    }

    private fun restoreAvatar() {
        viewModelScope.launch {
            val savedUri = sessionManager.getAvatarUri()
            if (!savedUri.isNullOrEmpty()) {
                _uiState.update { state -> state.copy(avatarUri = Uri.parse(savedUri)) }
            }
        }
    }


    /**
     * Cierra sesión limpiando el token y devolviendo el control a la UI.
     * ProfileScreen usa este método para volver a la pantalla de login.
     */
    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearAuthToken()
            onLoggedOut()
        }
    }
}
