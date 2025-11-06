package com.example.modaurbana.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



/**
 * Estado de la UI (EP3: /me + imagen de perfil almacenada localmente)
 * Guía 12: gestión de estados; Guía 13: cámara/galería -> Uri del avatar.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val formattedCreatedAt: String = "",
    val avatarUri: Uri? = null  // ✨ Nuevo campo
)

/**
 * ViewModel con Application para acceder a contexto (SessionManager / DataStore).
 * Mantiene compatibilidad con tu UserRepository(application).
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)
    private val sessionManager = SessionManager(getApplication()) // Guía API REST (token), Guía 12 (persistencia)

    // Estado PRIVADO (solo el VM lo modifica)
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado PÚBLICO (la UI lo observa)
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * Carga el usuario autenticado.
     * EP3 sugiere usar /me; si tu repositorio aún no lo expone, sigue usando fetchUser(id).
     */
    fun loadUser(idFallback: Int = 1) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // TODO: si tu UserRepository implementa fetchMe(), úsalo aquí:
            // val result = repository.fetchMe()
            val result = repository.fetchUser(idFallback)

            _uiState.update { current ->
                result.fold(
                    onSuccess = { user ->
                        current.copy(
                            isLoading = false,
                            userName = user.name,
                            userEmail = user.email ?: "Sin email",
                            error = null
                        )
                    },
                    onFailure = { e ->
                        current.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Error desconocido"
                        )
                    }
                )
            }

            // Intentar restaurar avatar persistido (Guía 12/EP3: persistencia local)
            restoreAvatarIfAny()
        }
    }

    /**
     * Actualiza el avatar en memoria (Compose reacciona) y opcionalmente lo persiste.
     * Guía 13: la imagen llega desde cámara/galería como Uri seguro.
     */
    fun updateAvatar(uri: Uri?) {
        _uiState.update { it.copy(avatarUri = uri) }
        // Persistencia básica para render offline (EP3):
        viewModelScope.launch {
            // TODO (opcional pero recomendado por EP3): guarda la cadena del Uri en DataStore
            // sessionManager.saveAvatarUri(uri?.toString() ?: "")
        }
    }

    /**
     * Intenta restaurar la URI del avatar desde almacenamiento local.
     * Guía 12: persistencia; EP3: imagen visible tras reinicio.
     */
    private fun restoreAvatarIfAny() {
        viewModelScope.launch {
            // TODO si implementas en SessionManager:
            // val saved = sessionManager.getAvatarUri()
            // if (!saved.isNullOrEmpty()) {
            //     _uiState.update { it.copy(avatarUri = Uri.parse(saved)) }
            // }
        }
    }

    /**
     * Cierre de sesión: limpia el token y deja la navegación a cargo de la UI.
     * (La pantalla debe navegar a Login y limpiar backstack — Guía 10)
     */
    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearAuthToken()
            onLoggedOut()
        }
    }
}
