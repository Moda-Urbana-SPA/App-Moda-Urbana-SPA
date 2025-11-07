package com.example.modaurbanaprototipoapp

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.avatarDataStore: DataStore<Preferences> by preferencesDataStore(name = "avatar_prefs")


class AvatarRepository(private val context: Context) {

    companion object {
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    }

    suspend fun saveAvatarUri(uri: Uri?) {
        context.avatarDataStore.edit { preferences ->
            if (uri != null) {
                preferences[AVATAR_URI_KEY] = uri.toString()
            } else {
                preferences.remove(AVATAR_URI_KEY)
            }
        }
    }

    fun getAvatarUri(): Flow<Uri?> {
        return context.avatarDataStore.data.map { preferences ->
            val uriString = preferences[AVATAR_URI_KEY]
            uriString?.let { Uri.parse(it) }
        }
    }

    suspend fun clearAvatar() {
        saveAvatarUri(null)
    }
}