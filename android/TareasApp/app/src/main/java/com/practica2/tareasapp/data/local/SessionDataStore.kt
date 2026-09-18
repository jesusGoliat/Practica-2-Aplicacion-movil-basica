package com.practica2.tareasapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.sessionDataStore by preferencesDataStore(name = "session")

/** Persiste el JWT en disco (DataStore) para que la sesion sobreviva a reinicios de la app. */
class SessionDataStore(private val context: Context) {

    private val tokenKey = stringPreferencesKey("access_token")

    suspend fun saveToken(token: String) {
        context.sessionDataStore.edit { prefs -> prefs[tokenKey] = token }
    }

    suspend fun clearToken() {
        context.sessionDataStore.edit { prefs -> prefs.remove(tokenKey) }
    }

    suspend fun readToken(): String? {
        return context.sessionDataStore.data.first()[tokenKey]
    }
}
