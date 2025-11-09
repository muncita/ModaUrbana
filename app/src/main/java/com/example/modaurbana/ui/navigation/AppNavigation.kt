package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.*
import com.example.modaurbana.viewmodel.AuthViewModel

/**
 * Controla la navegación entre pantallas.
 * Usa el mismo ViewModel (AuthViewModel) para compartir el estado de autenticación.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,   // ✅ ← este parámetro faltaba
    vm: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.route
    ) {
        // 🔹 Pantalla de inicio de sesión
        composable(Route.Login.route) {
            LoginScreen(navController = navController, vm = vm)
        }

        // 🔹 Pantalla de registro de nuevos usuarios
        composable(Route.Register.route) {
            RegisterScreen(
                navController = navController,
                vm = vm,
                onRegisterSuccess = {
                    // Al registrarse con éxito → ir a Home
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🔹 Pantalla principal o Home
        composable(Route.Home.route) {
            HomeScreen(navController = navController, vm = vm)
        }

        // 🔹 Pantalla de perfil del usuario
        composable(Route.Profile.route) {
            ProfileScreen(navController = navController, vm = vm)
        }
    }
}
