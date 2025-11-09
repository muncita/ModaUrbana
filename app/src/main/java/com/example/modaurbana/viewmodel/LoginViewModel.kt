package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val session = SessionManager(app.applicationContext)
    private val repo = UserRepository()

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val response = repo.login(email, password)
                session.saveAuthToken(response.authToken ?: "")
                _state.value = LoginUiState(success = true)
            } catch (e: Exception) {
                _state.value = LoginUiState(error = e.message ?: "Error desconocido")
            }
        }
    }
}

data class LoginUiState(
    val success: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
