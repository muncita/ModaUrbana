package com.example.modaurbana.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull



private val Context.dataStore by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")
    }


    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }


    suspend fun getAuthToken(): String? {
        val prefs = context.dataStore.data.map { it[KEY_AUTH_TOKEN] }
        return prefs.firstOrNull()
    }


    val authTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTH_TOKEN]
    }


    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
        }
    }


    suspend fun saveAvatarUri(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AVATAR_URI] = uri
        }
    }


    val avatarUriFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_AVATAR_URI] ?: ""
    }


    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
