package com.example.modaurbana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modaurbana.data.local.SessionManager
import com.example.modaurbana.ui.navigation.AppNavHost
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.ui.theme.ModaUrbanaTheme
import com.example.modaurbana.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa SessionManager para Retrofit y DataStore
        SessionManager.init(applicationContext)

        enableEdgeToEdge() // (opcional: para usar diseño de borde a borde)
        setContent {
            ModaUrbanaTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val ui by authViewModel.ui.collectAsState()

                // Define la pantalla inicial (Home si hay sesión, Login si no)
                val startDestination = if (ui.user != null) {
                    Route.Home.route
                } else {
                    Route.Login.route
                }

                // ✅ Llama correctamente al NavHost con el controlador
                AppNavHost(navController = navController, startDestination = startDestination)
            }
        }
    }
}
