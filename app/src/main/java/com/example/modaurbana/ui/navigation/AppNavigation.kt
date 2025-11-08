package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.*

/**
 * Controla la navegación principal de la app.
 * Define las pantallas y sus rutas según la clase Route.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Pantallas del flujo de autenticación
        composable(Route.Login.route) {
            LoginScreen(navController)
        }
        composable(Route.Register.route) {
            RegisterScreen(navController)
        }

        // Pantalla principal (inicio tras login)
        composable(Route.Home.route) {
            HomeScreen(navController)
        }

        // Perfil del usuario (foto, cámara, galería)
        composable(Route.Profile.route) {
            ProfileScreen()
        }
    }
}
