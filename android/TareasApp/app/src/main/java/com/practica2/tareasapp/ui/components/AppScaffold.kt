package com.practica2.tareasapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.practica2.tareasapp.ui.navigation.Routes

/**
 * Scaffold compartido por todas las pantallas: incluye el menu desplegable de
 * navegacion (Ejercicio 1) con las opciones de Login, Registro y CRUD de tareas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu de navegacion")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        if (!isLoggedIn) {
                            DropdownMenuItem(
                                text = { Text("Inicio de sesion") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Routes.LOGIN)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Registro de usuario") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Routes.REGISTER)
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Operaciones CRUD (Tareas)") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Routes.TAREAS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cerrar sesion") },
                                onClick = {
                                    menuExpanded = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding -> content(padding) }
}
