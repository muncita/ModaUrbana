package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.LoginScreen
import com.example.modaurbana.ui.screens.RegisterScreen
import com.example.modaurbana.ui.screens.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Route.Login.path)    { LoginScreen(navController) }
        composable(Route.Register.path) { RegisterScreen(navController) }
        composable(Route.Profile.path)  { ProfileScreen() }
    }
}