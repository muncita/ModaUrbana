package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val session = SessionManager(app.applicationContext)
    private val repo = AuthRepository(session)

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _state.value = RegisterUiState(isLoading = true)
                val user = repo.register(name, email, password)
                _state.value = RegisterUiState(success = true)
            } catch (e: Exception) {
                _state.value = RegisterUiState(error = e.message)
            }
        }
    }
}

data class RegisterUiState(
    val success: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
