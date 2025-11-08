package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.models.User
import com.example.modaurbana.repository.AuthRepository
import com.example.modaurbana.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",

    val nameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val confirmError: String? = null,

    val loading: Boolean = false,
    val errorMessage: String? = null,
    val loggedUser: User? = null
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)
    private val repo = AuthRepository(session)

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    init {
        // Si hay token, intentamos obtener perfil
        viewModelScope.launch {
            runCatching { repo.me() }
                .onSuccess { user -> _ui.value = _ui.value.copy(loggedUser = user) }
                .onFailure { /* No logged in, ignore */ }
        }
    }

    fun onName(s: String) { _ui.value = _ui.value.copy(name = s, nameError = null) }
    fun onEmail(s: String) { _ui.value = _ui.value.copy(email = s, emailError = null) }
    fun onPass(s: String) { _ui.value = _ui.value.copy(password = s, passError = null) }
    fun onConfirm(s: String) { _ui.value = _ui.value.copy(confirm = s, confirmError = null) }

    private fun validateRegister(): Boolean {
        var ok = true
        var st = _ui.value

        if (!ValidationUtils.isNotBlank(st.name)) { st = st.copy(nameError = "Nombre requerido"); ok = false }
        if (!ValidationUtils.isValidEmail(st.email)) { st = st.copy(emailError = "Email inválido"); ok = false }
        if (!ValidationUtils.isValidPassword(st.password)) { st = st.copy(passError = "Mínimo 6 caracteres"); ok = false }
        if (st.password != st.confirm) { st = st.copy(confirmError = "Las contraseñas no coinciden"); ok = false }

        _ui.value = st
        return ok
    }

    private fun validateLogin(): Boolean {
        var ok = true
        var st = _ui.value

        if (!ValidationUtils.isValidEmail(st.email)) { st = st.copy(emailError = "Email inválido"); ok = false }
        if (!ValidationUtils.isValidPassword(st.password)) { st = st.copy(passError = "Mínimo 6 caracteres"); ok = false }

        _ui.value = st
        return ok
    }

    fun doRegister(onSuccess: () -> Unit) {
        if (!validateRegister()) return
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, errorMessage = null)
            runCatching { repo.register(_ui.value.name, _ui.value.email, _ui.value.password) }
                .onSuccess { user -> _ui.value = _ui.value.copy(loading = false, loggedUser = user); onSuccess() }
                .onFailure { e -> _ui.value = _ui.value.copy(loading = false, errorMessage = e.message) }
        }
    }

    fun doLogin(onSuccess: () -> Unit) {
        if (!validateLogin()) return
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, errorMessage = null)
            runCatching { repo.login(_ui.value.email, _ui.value.password) }
                .onSuccess { user -> _ui.value = _ui.value.copy(loading = false, loggedUser = user); onSuccess() }
                .onFailure { e -> _ui.value = _ui.value.copy(loading = false, errorMessage = e.message) }
        }
    }
    fun doLogout() {
        viewModelScope.launch { repo.logout() }
        _ui.value = AuthUiState() // limpiar estado
    }

}
