package com.example.modaurbanaprototipoapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "session_prefs")

        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_USERNAME   = stringPreferencesKey("username")
        private val KEY_IS_LOGGED  = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTH_TOKEN] = token
        }
    }

    suspend fun saveDummyUserSession(username: String, token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USERNAME]   = username
            prefs[KEY_AUTH_TOKEN] = token
            prefs[KEY_IS_LOGGED]  = true
        }
    }

    suspend fun getAuthToken(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[KEY_AUTH_TOKEN] }
            .first()
    }

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_IS_LOGGED] ?: false }

    val username: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_USERNAME] }

    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USERNAME)
            prefs.remove(KEY_AUTH_TOKEN)
            prefs.remove(KEY_IS_LOGGED)
        }
    }
}
