package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.*

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination, modifier) {
        composable(Route.Login.route) { LoginScreen(navController) }
        composable(Route.Register.route) { RegisterScreen(navController) }
        composable(Route.Home.route) { HomeScreen(navController) }
        composable(Route.Profile.route) { ProfileScreen() }
    }
}
