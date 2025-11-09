package com.example.modaurbana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.ui.navigation.AppNavigation
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.ui.theme.ModaUrbanaTheme
import com.example.modaurbana.viewmodel.AuthViewModel

/**
 * MainActivity:
 * - Inicializa SessionManager
 * - Controla el flujo de inicio de sesión y navegación
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ModaUrbanaTheme {
                // ✅ Crear el controlador de navegación
                val navController = rememberNavController()

                // ✅ Inicializar el ViewModel correctamente
                val authViewModel: AuthViewModel = viewModel()

                // ✅ Observar el estado actual del usuario
                val uiState by authViewModel.ui.collectAsState()

                // ✅ Determinar pantalla inicial según sesión
                val startDestination = if (uiState.user != null) {
                    Route.Profile.route
                } else {
                    Route.Login.route
                }

                // ✅ Configurar la navegación principal
                AppNavigation(
                    navController = navController,
                    vm = authViewModel
                )
            }
        }
    }
}
