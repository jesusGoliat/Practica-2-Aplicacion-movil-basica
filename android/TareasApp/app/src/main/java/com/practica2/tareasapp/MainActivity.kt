package com.practica2.tareasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica2.tareasapp.data.local.SessionDataStore
import com.practica2.tareasapp.ui.navigation.AppNavGraph
import com.practica2.tareasapp.ui.theme.TareasAppTheme
import com.practica2.tareasapp.viewmodel.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TareasAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PracticaApp()
                }
            }
        }
    }
}

@Composable
private fun PracticaApp() {
    val sessionDataStore = SessionDataStore(LocalContext.current)
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModel.factory(sessionDataStore))
    AppNavGraph(sessionViewModel = sessionViewModel)
}
