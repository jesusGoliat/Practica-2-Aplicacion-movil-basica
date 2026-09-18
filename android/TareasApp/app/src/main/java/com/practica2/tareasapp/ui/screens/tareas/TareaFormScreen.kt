package com.practica2.tareasapp.ui.screens.tareas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practica2.tareasapp.core.UiState
import com.practica2.tareasapp.viewmodel.TareasViewModel

@Composable
fun TareaFormScreen(
    modifier: Modifier = Modifier,
    viewModel: TareasViewModel,
    tareaId: Int?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onSessionExpired: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var completada by remember { mutableStateOf(false) }
    var yaCargado by remember { mutableStateOf(false) }

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    val sessionExpired by viewModel.sessionExpired.collectAsStateWithLifecycle()

    LaunchedEffect(tareaId) { viewModel.cargarTareaParaEditar(tareaId) }

    LaunchedEffect(formState) {
        val current = formState
        if (current is UiState.Success && !yaCargado) {
            current.data?.let { tarea ->
                titulo = tarea.titulo
                descripcion = tarea.descripcion.orEmpty()
                completada = tarea.completada
            }
            yaCargado = true
        }
    }

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            viewModel.resetSaveState()
            onSaved()
        }
    }

    LaunchedEffect(sessionExpired) {
        if (sessionExpired) {
            viewModel.consumirSessionExpired()
            onSessionExpired()
        }
    }

    if (formState is UiState.Loading) {
        CircularProgressIndicator(modifier = modifier.padding(24.dp))
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Titulo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (tareaId != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Completada")
                Switch(checked = completada, onCheckedChange = { completada = it })
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        val currentSave = saveState
        if (currentSave is UiState.Error) {
            Text(
                text = currentSave.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = { viewModel.guardarTarea(tareaId, titulo, descripcion, completada) },
            enabled = saveState !is UiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (saveState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp))
            } else {
                Text(if (tareaId == null) "Crear tarea" else "Guardar cambios")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}
