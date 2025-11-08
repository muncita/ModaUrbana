package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.AuthRepository
import com.example.modaurbana.repository.UserRepository
import com.example.modaurbana.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI del Home.
 * Muestra al usuario logueado y permite cerrar sesión.
 */
data class HomeUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel del Home: obtiene datos del usuario y maneja logout.
 */
class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val userRepo = UserRepository(session)
    private val authRepo = AuthRepository(session)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    /**
     * Obtiene los datos del usuario autenticado desde la API (/auth/me)
     */
    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            val result = userRepo.fetchMe()
            _uiState.value = result.fold(
                onSuccess = { HomeUiState(user = it) },
                onFailure = { e -> HomeUiState(error = e.localizedMessage) }
            )
        }
    }

    /**
     * Limpia el token y cierra la sesión
     */
    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _uiState.value = HomeUiState(user = null)
        }
    }
}
