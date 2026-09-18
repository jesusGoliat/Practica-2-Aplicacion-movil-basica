package com.practica2.tareasapp.data.remote

import com.practica2.tareasapp.data.remote.dto.LoginRequest
import com.practica2.tareasapp.data.remote.dto.RegisterRequest
import com.practica2.tareasapp.data.remote.dto.TareaRequest
import com.practica2.tareasapp.data.remote.dto.TareaResponse
import com.practica2.tareasapp.data.remote.dto.TareaUpdateRequest
import com.practica2.tareasapp.data.remote.dto.TokenResponse
import com.practica2.tareasapp.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @GET("tareas")
    suspend fun listarTareas(): Response<List<TareaResponse>>

    @GET("tareas/{id}")
    suspend fun obtenerTarea(@Path("id") id: Int): Response<TareaResponse>

    @POST("tareas")
    suspend fun crearTarea(@Body request: TareaRequest): Response<TareaResponse>

    @PUT("tareas/{id}")
    suspend fun actualizarTarea(
        @Path("id") id: Int,
        @Body request: TareaUpdateRequest
    ): Response<TareaResponse>

    @DELETE("tareas/{id}")
    suspend fun borrarTarea(@Path("id") id: Int): Response<Unit>
}
