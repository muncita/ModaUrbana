package com.example.modaurbana.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbana.data.local.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val session = SessionManager(app.applicationContext)

    val avatarUri = session.avatarUriFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    fun setAvatar(uri: String) {
        viewModelScope.launch {
            session.saveAvatarUri(uri)
        }
    }

    fun clearAvatar() {
        viewModelScope.launch {
            session.saveAvatarUri("")
        }
    }
}
