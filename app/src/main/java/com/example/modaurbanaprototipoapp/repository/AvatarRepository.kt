package com.example.modaurbanaprototipoapp.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.avatarDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "avatar_preferences"
)

class AvatarRepository(private val context: Context) {

    companion object {
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri_key")
    }

    fun getAvatarUri(): Flow<Uri?> {
        return context.avatarDataStore.data.map { preferences ->
            preferences[AVATAR_URI_KEY]?.let { uriString ->
                Uri.parse(uriString)
            }
        }
    }

    suspend fun saveAvatarUri(uri: Uri?) {
        if (uri != null) {
            context.avatarDataStore.edit { preferences ->
                preferences[AVATAR_URI_KEY] = uri.toString()
            }
        } else {
            clearAvatarUri()
        }
    }

    suspend fun clearAvatarUri() {
        context.avatarDataStore.edit { preferences ->
            preferences.remove(AVATAR_URI_KEY)
        }
    }
}