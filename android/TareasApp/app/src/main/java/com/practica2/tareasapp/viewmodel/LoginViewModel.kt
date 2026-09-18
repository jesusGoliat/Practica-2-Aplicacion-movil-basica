package com.practica2.tareasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.practica2.tareasapp.core.ApiResult
import com.practica2.tareasapp.core.UiState
import com.practica2.tareasapp.data.remote.dto.TokenResponse
import com.practica2.tareasapp.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _state = MutableStateFlow<UiState<TokenResponse>>(UiState.Idle)
    val state: StateFlow<UiState<TokenResponse>> = _state.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = UiState.Error("El usuario y la contrasena son obligatorios")
            return
        }
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = when (val result = repository.login(username.trim(), password)) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Error -> UiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _state.value = UiState.Idle
    }
}
