package com.example.modaurbana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.ui.navigation.AppNavigation
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.ui.theme.ModaUrbanaTheme
import kotlinx.coroutines.runBlocking

/**
 * Punto de entrada principal de la aplicación ModaUrbana.
 * Inicializa el SessionManager y decide la pantalla inicial según el token.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa el contexto global de SessionManager
        SessionManager.init(this)

        enableEdgeToEdge()
        setContent {
            ModaUrbanaTheme {
                val navController = rememberNavController()

                // Determina la ruta inicial según si existe un token guardado
                var startDestination by remember { mutableStateOf(Route.Login.path) }

                LaunchedEffect(Unit) {
                    val token = runBlocking { SessionManager(this@MainActivity).getAuthToken() }
                    if (!token.isNullOrEmpty()) {
                        startDestination = Route.Profile.path
                    }
                }

                // Lanza la navegación de la app
                AppNavigation(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}
