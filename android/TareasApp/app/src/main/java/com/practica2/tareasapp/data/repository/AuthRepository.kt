package com.practica2.tareasapp.data.repository

import java.io.IOException
import com.practica2.tareasapp.core.ApiResult
import com.practica2.tareasapp.data.remote.ApiService
import com.practica2.tareasapp.data.remote.RetrofitClient
import com.practica2.tareasapp.data.remote.dto.LoginRequest
import com.practica2.tareasapp.data.remote.dto.RegisterRequest
import com.practica2.tareasapp.data.remote.dto.TokenResponse
import com.practica2.tareasapp.data.remote.dto.UserResponse
import com.practica2.tareasapp.data.remote.parseErrorMessage

class AuthRepository(private val api: ApiService = RetrofitClient.apiService) {

    suspend fun register(username: String, password: String): ApiResult<UserResponse> {
        return try {
            val response = api.register(RegisterRequest(username, password))
            val body = response.body()
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

    suspend fun login(username: String, password: String): ApiResult<TokenResponse> {
        return try {
            val response = api.login(LoginRequest(username, password))
            val body = response.body()
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
