package com.practica2.tareasapp.data.remote

/**
 * URL base del backend REST (FastAPI + PostgreSQL, backend/ en la raiz del repo).
 *
 * Editar segun el entorno de pruebas:
 * - Dispositivo fisico en la MISMA red Wi-Fi que el equipo donde corre
 *   "docker compose up --build": usar la IP local de ese equipo, por ejemplo
 *   "http://192.168.1.100:8000/". Obtener la IP con `ip addr` (Linux/Mac) o
 *   `ipconfig` (Windows), y verificar que el puerto 8000 este accesible en la
 *   red (firewall permitiendo conexiones entrantes).
 * - Emulador de Android Studio (no usado en el flujo de pruebas de esta
 *   entrega, pero documentado por completitud): "http://10.0.2.2:8000/",
 *   ya que "localhost" desde el emulador apunta al propio emulador y no al
 *   equipo anfitrion.
 */
object ApiConfig {
    const val BASE_URL = "http://10.62.186.248:8000/"
}
