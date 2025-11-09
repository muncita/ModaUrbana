package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val session = SessionManager(app.applicationContext)

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri: StateFlow<String?> = _avatarUri

    fun saveAvatarUri(uri: String) {
        viewModelScope.launch {
            _avatarUri.value = uri
        }
    }

    fun clearAvatar() {
        viewModelScope.launch {
            _avatarUri.value = null
        }
    }
}
