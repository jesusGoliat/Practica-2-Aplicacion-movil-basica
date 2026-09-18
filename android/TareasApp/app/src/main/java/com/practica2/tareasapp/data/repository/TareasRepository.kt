package com.practica2.tareasapp.data.repository

import java.io.IOException
import com.practica2.tareasapp.core.ApiResult
import com.practica2.tareasapp.data.remote.ApiService
import com.practica2.tareasapp.data.remote.RetrofitClient
import com.practica2.tareasapp.data.remote.dto.TareaRequest
import com.practica2.tareasapp.data.remote.dto.TareaResponse
import com.practica2.tareasapp.data.remote.dto.TareaUpdateRequest
import com.practica2.tareasapp.data.remote.parseErrorMessage
import retrofit2.Response

class TareasRepository(private val api: ApiService = RetrofitClient.apiService) {

    suspend fun listar(): ApiResult<List<TareaResponse>> = safeCall { api.listarTareas() }

    suspend fun obtener(id: Int): ApiResult<TareaResponse> = safeCall { api.obtenerTarea(id) }

    suspend fun crear(titulo: String, descripcion: String?): ApiResult<TareaResponse> =
        safeCall { api.crearTarea(TareaRequest(titulo = titulo, descripcion = descripcion)) }

    suspend fun actualizar(
        id: Int,
        titulo: String?,
        descripcion: String?,
        completada: Boolean?
    ): ApiResult<TareaResponse> = safeCall {
        api.actualizarTarea(id, TareaUpdateRequest(titulo, descripcion, completada))
    }

    suspend fun borrar(id: Int): ApiResult<Unit> = safeCall(defaultOnSuccess = Unit) { api.borrarTarea(id) }

    private suspend fun <T> safeCall(
        defaultOnSuccess: T? = null,
        call: suspend () -> Response<T>
    ): ApiResult<T> {
        return try {
            val response = call()
            val body = response.body() ?: defaultOnSuccess
            if (response.isSuccessful && body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(parseErrorMessage(response), response.code())
            }
        } catch (e: IOException) {
            ApiResult.Error("No se pudo conectar con el servidor. Verifica la URL base y tu conexion de red.")
        } catch (e: Exception) {
            ApiResult.Error("Ocurrio un error inesperado: ${e.localizedMessage}")
        }
    }
}
