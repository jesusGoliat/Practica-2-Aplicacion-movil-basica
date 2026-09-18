package com.practica2.tareasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.practica2.tareasapp.core.ApiResult
import com.practica2.tareasapp.core.UiState
import com.practica2.tareasapp.core.isUnauthorized
import com.practica2.tareasapp.data.remote.dto.TareaResponse
import com.practica2.tareasapp.data.repository.TareasRepository

class TareasViewModel(private val repository: TareasRepository = TareasRepository()) : ViewModel() {

    private val _tareasState = MutableStateFlow<UiState<List<TareaResponse>>>(UiState.Idle)
    val tareasState: StateFlow<UiState<List<TareaResponse>>> = _tareasState.asStateFlow()

    private val _formState = MutableStateFlow<UiState<TareaResponse?>>(UiState.Idle)
    val formState: StateFlow<UiState<TareaResponse?>> = _formState.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val saveState: StateFlow<UiState<Unit>> = _saveState.asStateFlow()

    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired: StateFlow<Boolean> = _sessionExpired.asStateFlow()

    fun cargarTareas() {
        viewModelScope.launch {
            _tareasState.value = UiState.Loading
            when (val result = repository.listar()) {
                is ApiResult.Success -> _tareasState.value = UiState.Success(result.data)
                is ApiResult.Error -> {
                    _tareasState.value = UiState.Error(result.message)
                    if (result.isUnauthorized()) _sessionExpired.value = true
                }
            }
        }
    }

    fun cargarTareaParaEditar(id: Int?) {
        if (id == null) {
            _formState.value = UiState.Success(null)
            return
        }
        viewModelScope.launch {
            _formState.value = UiState.Loading
            when (val result = repository.obtener(id)) {
                is ApiResult.Success -> _formState.value = UiState.Success(result.data)
                is ApiResult.Error -> {
                    _formState.value = UiState.Error(result.message)
                    if (result.isUnauthorized()) _sessionExpired.value = true
                }
            }
        }
    }

    fun guardarTarea(id: Int?, titulo: String, descripcion: String, completada: Boolean) {
        if (titulo.isBlank()) {
            _saveState.value = UiState.Error("El titulo es obligatorio")
            return
        }
        val descripcionFinal = descripcion.ifBlank { null }
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            val result = if (id == null) {
                repository.crear(titulo.trim(), descripcionFinal)
            } else {
                repository.actualizar(id, titulo.trim(), descripcionFinal, completada)
            }
            when (result) {
                is ApiResult.Success -> _saveState.value = UiState.Success(Unit)
                is ApiResult.Error -> {
                    _saveState.value = UiState.Error(result.message)
                    if (result.isUnauthorized()) _sessionExpired.value = true
                }
            }
        }
    }

    fun alternarCompletada(tarea: TareaResponse) {
        viewModelScope.launch {
            when (val result = repository.actualizar(tarea.id, null, null, !tarea.completada)) {
                is ApiResult.Success -> cargarTareas()
                is ApiResult.Error -> {
                    _tareasState.value = UiState.Error(result.message)
                    if (result.isUnauthorized()) _sessionExpired.value = true
                }
            }
        }
    }

    fun borrarTarea(id: Int) {
        viewModelScope.launch {
            when (val result = repository.borrar(id)) {
                is ApiResult.Success -> cargarTareas()
                is ApiResult.Error -> {
                    _tareasState.value = UiState.Error(result.message)
                    if (result.isUnauthorized()) _sessionExpired.value = true
                }
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = UiState.Idle
    }

    fun consumirSessionExpired() {
        _sessionExpired.value = false
    }
}
