package com.practica2.tareasapp.core

/** Resultado de una llamada de red ya interpretado, sin exponer detalles de Retrofit. */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}

/** true si el error corresponde a una sesion invalida o expirada (401). */
fun ApiResult.Error.isUnauthorized(): Boolean = code == 401
