package com.example.modaurbana.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Clase responsable de gestionar la sesión del usuario:
 * - Guarda y obtiene el token JWT
 * - Guarda y obtiene la URI del avatar de perfil
 * - Puede limpiarse al cerrar sesión
 */
class SessionManager(private val context: Context) {

    companion object {
        // DataStore vinculado al contexto
        private val Context.dataStore by preferencesDataStore(name = "session_prefs")

        // Claves de preferencias
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")

        /**
         * Contexto global opcional para inicializar SessionManager
         * sin necesidad de un Application directamente.
         */
        lateinit var appContext: Context
        fun init(context: Context) {
            appContext = context.applicationContext
        }
    }

    // --- TOKEN ---

    /** Guarda el token JWT */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    /** Obtiene el token JWT guardado */
    suspend fun getAuthToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[KEY_AUTH_TOKEN] }
            .first()
    }

    /** Elimina el token JWT (logout) */
    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
        }
    }

    // --- AVATAR ---

    /** Guarda la URI del avatar como String */
    suspend fun saveAvatarUri(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AVATAR_URI] = uri
        }
    }

    /** Obtiene la URI del avatar guardado */
    suspend fun getAvatarUri(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[KEY_AVATAR_URI] }
            .first()
    }

    /** Limpia toda la sesión (token + avatar) */
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
