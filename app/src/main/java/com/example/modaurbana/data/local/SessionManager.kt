package com.example.modaurbana.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")
    }

    val authTokenFlow = context.dataStore.data.map { it[KEY_TOKEN] ?: "" }
    val avatarUriFlow = context.dataStore.data.map { it[KEY_AVATAR_URI] ?: "" }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[KEY_TOKEN] = token }
    }

    suspend fun getAuthToken(): String =
        context.dataStore.data.map { it[KEY_TOKEN] ?: "" }.first()

    suspend fun clearAuthToken() {
        context.dataStore.edit { it.remove(KEY_TOKEN) }
    }

    suspend fun saveAvatarUri(uri: String) {
        context.dataStore.edit { it[KEY_AVATAR_URI] = uri }
    }
}
