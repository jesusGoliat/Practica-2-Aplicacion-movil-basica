package com.practica2.tareasapp.data.remote

import org.json.JSONObject
import retrofit2.Response

/**
 * Extrae el mensaje "detail" que envia FastAPI en las respuestas de error
 * (ver backend/app/routers y backend/app/security.py), con un mensaje generico
 * de respaldo si el cuerpo no trae ese campo.
 */
fun parseErrorMessage(response: Response<*>): String {
    val errorBody = response.errorBody()?.string()
    if (errorBody.isNullOrBlank()) return "Error del servidor (${response.code()})"
    return try {
        JSONObject(errorBody).optString("detail").ifBlank { "Error del servidor (${response.code()})" }
    } catch (e: Exception) {
        "Error del servidor (${response.code()})"
    }
}
