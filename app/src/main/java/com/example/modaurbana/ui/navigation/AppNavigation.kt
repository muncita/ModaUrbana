package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.LoginScreen
import com.example.modaurbana.ui.screens.ProfileScreen
import com.example.modaurbana.ui.screens.RegisterScreen

/**
 * NavHost con startDestination condicionado por sesión persistida (opcional).
 * Implementa auto-login: si hay token en SessionManager -> Profile como inicio.
 * (EP3 pide back correcto y flujo principal) (Guía 10).
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    start: String // pásale Route.Login.path o Route.Profile.path según SessionManager
) {
    NavHost(navController = navController, startDestination = start) {
        composable(Route.Login.path)    { LoginScreen(navController) }
        composable(Route.Register.path) { RegisterScreen(navController) }
        composable(Route.Profile.path)  { ProfileScreen(navController) }
    }
}
