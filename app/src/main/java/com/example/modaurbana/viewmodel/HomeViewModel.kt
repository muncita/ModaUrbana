package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val session = SessionManager(app.applicationContext)
    private val repo = UserRepository()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    fun loadUser() {
        viewModelScope.launch {
            try {
                val token = session.getAuthToken()
                if (!token.isNullOrEmpty()) {
                    val user = repo.getCurrentUser(token)
                    _username.value = user.name ?: "Usuario"
                } else {
                    _username.value = "Invitado"
                }
            } catch (e: Exception) {
                _username.value = "Error: ${e.message}"
            }
        }
    }
}
