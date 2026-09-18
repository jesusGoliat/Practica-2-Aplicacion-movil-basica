package com.practica2.tareasapp.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val TAREAS = "tareas"
    const val TAREA_FORM_BASE = "tarea_form"
    const val TAREA_FORM = "$TAREA_FORM_BASE?tareaId={tareaId}"

    fun tareaForm(tareaId: Int? = null) = "$TAREA_FORM_BASE?tareaId=${tareaId ?: -1}"
}
