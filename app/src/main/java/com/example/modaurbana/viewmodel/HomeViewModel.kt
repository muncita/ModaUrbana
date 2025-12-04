package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    app: Application,
    private val session: SessionManager,
    private val repo: UserRepository
) : AndroidViewModel(app) {

    // 👇 constructor que usa la app “de verdad”
    constructor(app: Application) : this(
        app,
        SessionManager(app.applicationContext),
        UserRepository()
    )

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
