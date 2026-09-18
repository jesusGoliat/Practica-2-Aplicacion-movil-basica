package com.practica2.tareasapp.ui.screens.tareas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica2.tareasapp.core.UiState
import com.practica2.tareasapp.data.remote.dto.TareaResponse
import com.practica2.tareasapp.viewmodel.TareasViewModel

@Composable
fun TareasListScreen(
    modifier: Modifier = Modifier,
    viewModel: TareasViewModel,
    onAddTarea: () -> Unit,
    onEditTarea: (Int) -> Unit,
    onSessionExpired: () -> Unit
) {
    val tareasState by viewModel.tareasState.collectAsStateWithLifecycle()
    val sessionExpired by viewModel.sessionExpired.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.cargarTareas() }

    LaunchedEffect(sessionExpired) {
        if (sessionExpired) {
            viewModel.consumirSessionExpired()
            onSessionExpired()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val current = tareasState) {
            is UiState.Loading, UiState.Idle -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = current.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.cargarTareas() }) {
                        Text("Reintentar")
                    }
                }
            }

            is UiState.Success -> {
                if (current.data.isEmpty()) {
                    Text(
                        text = "Aun no tienes tareas. Usa el boton + para crear una.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
                    ) {
                        items(current.data, key = { it.id }) { tarea ->
                            TareaItem(
                                tarea = tarea,
                                onToggle = { viewModel.alternarCompletada(tarea) },
                                onEdit = { onEditTarea(tarea.id) },
                                onDelete = { viewModel.borrarTarea(tarea.id) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddTarea,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nueva tarea")
        }
    }
}

@Composable
private fun TareaItem(
    tarea: TareaResponse,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(checked = tarea.completada, onCheckedChange = { onToggle() })
                Column {
                    Text(
                        text = tarea.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                    )
                    if (!tarea.descripcion.isNullOrBlank()) {
                        Text(
                            text = tarea.descripcion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar tarea")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Borrar tarea")
            }
        }
    }
}
