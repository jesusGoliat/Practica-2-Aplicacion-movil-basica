package com.practica2.tareasapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TareaRequest(
    val titulo: String,
    val descripcion: String?,
    val completada: Boolean = false
)

data class TareaUpdateRequest(
    val titulo: String? = null,
    val descripcion: String? = null,
    val completada: Boolean? = null
)

data class TareaResponse(
    val id: Int,
    val titulo: String,
    val descripcion: String?,
    val completada: Boolean,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
