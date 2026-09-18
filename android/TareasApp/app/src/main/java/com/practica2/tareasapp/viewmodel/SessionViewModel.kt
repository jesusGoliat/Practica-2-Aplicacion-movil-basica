package com.practica2.tareasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.practica2.tareasapp.data.local.SessionDataStore
import com.practica2.tareasapp.data.local.TokenHolder

/**
 * Fuente de verdad de la sesion actual. null = todavia no se leyo el token
 * persistido (pantalla de carga inicial); true/false = sesion activa o no.
 */
class SessionViewModel(private val sessionDataStore: SessionDataStore) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            val savedToken = sessionDataStore.readToken()
            TokenHolder.token = savedToken
            _isLoggedIn.value = savedToken != null
        }
    }

    fun onLoginSuccess(token: String) {
        TokenHolder.token = token
        viewModelScope.launch { sessionDataStore.saveToken(token) }
        _isLoggedIn.value = true
    }

    fun logout() {
        TokenHolder.token = null
        viewModelScope.launch { sessionDataStore.clearToken() }
        _isLoggedIn.value = false
    }

    companion object {
        fun factory(sessionDataStore: SessionDataStore): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SessionViewModel(sessionDataStore) as T
                }
            }
    }
}
