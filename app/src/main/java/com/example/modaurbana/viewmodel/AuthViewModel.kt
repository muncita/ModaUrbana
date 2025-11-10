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


class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val repo = AuthRepository(session)

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                val loginResponse = repo.login(email, password)

                val user = repo.currentUser()

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
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, error = null)

                val registerResponse = repo.register(name, email, password)

                val user = repo.currentUser()

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


data class AuthUiState(
    val user: UserResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

