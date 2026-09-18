package com.practica2.tareasapp.data.local

/** Copia en memoria del token de sesion, leida por el interceptor de Retrofit. */
object TokenHolder {
    @Volatile
    var token: String? = null
}
