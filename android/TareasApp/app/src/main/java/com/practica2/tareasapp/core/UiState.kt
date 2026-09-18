package com.practica2.tareasapp.core

/** Estado de UI generico: exactamente lo que Ejercicio 1 pide manejar (carga/error/exito). */
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
