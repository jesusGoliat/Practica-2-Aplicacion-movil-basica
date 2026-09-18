package com.practica2.tareasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.practica2.tareasapp.core.ApiResult
import com.practica2.tareasapp.core.UiState
import com.practica2.tareasapp.data.remote.dto.UserResponse
import com.practica2.tareasapp.data.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _state = MutableStateFlow<UiState<UserResponse>>(UiState.Idle)
    val state: StateFlow<UiState<UserResponse>> = _state.asStateFlow()

    fun register(username: String, password: String) {
        if (username.trim().length < 3) {
            _state.value = UiState.Error("El usuario debe tener al menos 3 caracteres")
            return
        }
        if (password.length < 8) {
            _state.value = UiState.Error("La contrasena debe tener al menos 8 caracteres")
            return
        }
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = when (val result = repository.register(username.trim(), password)) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Error -> UiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _state.value = UiState.Idle
    }
}
