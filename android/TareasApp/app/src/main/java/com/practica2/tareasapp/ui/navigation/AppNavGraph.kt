package com.practica2.tareasapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practica2.tareasapp.ui.components.AppScaffold
import com.practica2.tareasapp.ui.screens.login.LoginScreen
import com.practica2.tareasapp.ui.screens.register.RegisterScreen
import com.practica2.tareasapp.ui.screens.tareas.TareaFormScreen
import com.practica2.tareasapp.ui.screens.tareas.TareasListScreen
import com.practica2.tareasapp.viewmodel.SessionViewModel
import com.practica2.tareasapp.viewmodel.TareasViewModel

@Composable
fun AppNavGraph(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()
    val tareasViewModel: TareasViewModel = viewModel()
    val loggedIn by sessionViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val currentlyLoggedIn = loggedIn
    if (currentlyLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    fun logoutAndGoToLogin() {
        sessionViewModel.logout()
        navController.navigate(Routes.LOGIN) { popUpTo(0) }
    }

    NavHost(
        navController = navController,
        startDestination = if (currentlyLoggedIn) Routes.TAREAS else Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            AppScaffold(
                title = "Inicio de sesion",
                navController = navController,
                isLoggedIn = false,
                onLogout = {}
            ) { padding ->
                LoginScreen(
                    modifier = Modifier.padding(padding),
                    onLoginSuccess = { token ->
                        sessionViewModel.onLoginSuccess(token)
                        navController.navigate(Routes.TAREAS) { popUpTo(0) }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
        }
        composable(Routes.REGISTER) {
            AppScaffold(
                title = "Registro de usuario",
                navController = navController,
                isLoggedIn = false,
                onLogout = {}
            ) { padding ->
                RegisterScreen(
                    modifier = Modifier.padding(padding),
                    onRegisterSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
                )
            }
        }
        composable(Routes.TAREAS) {
            AppScaffold(
                title = "Mis tareas",
                navController = navController,
                isLoggedIn = true,
                onLogout = { logoutAndGoToLogin() }
            ) { padding ->
                TareasListScreen(
                    modifier = Modifier.padding(padding),
                    viewModel = tareasViewModel,
                    onAddTarea = { navController.navigate(Routes.tareaForm()) },
                    onEditTarea = { id -> navController.navigate(Routes.tareaForm(id)) },
                    onSessionExpired = { logoutAndGoToLogin() }
                )
            }
        }
        composable(
            route = Routes.TAREA_FORM,
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val rawId = backStackEntry.arguments?.getInt("tareaId") ?: -1
            val tareaId = if (rawId == -1) null else rawId
            AppScaffold(
                title = if (tareaId == null) "Nueva tarea" else "Editar tarea",
                navController = navController,
                isLoggedIn = true,
                onLogout = { logoutAndGoToLogin() }
            ) { padding ->
                TareaFormScreen(
                    modifier = Modifier.padding(padding),
                    viewModel = tareasViewModel,
                    tareaId = tareaId,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() },
                    onSessionExpired = { logoutAndGoToLogin() }
                )
            }
        }
    }
}
