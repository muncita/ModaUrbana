package com.example.modaurbana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modaurbana.ui.screens.*
import com.example.modaurbana.viewmodel.AuthViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    vm: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.route
    ) {
        composable(Route.Login.route) {
            LoginScreen(navController = navController, vm = vm)
        }


        composable(Route.Register.route) {
            RegisterScreen(
                navController = navController,
                vm = vm,
                onRegisterSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(Route.Home.route) {
            HomeScreen(navController = navController, vm = vm)
        }


        composable(Route.Profile.route) {
            ProfileScreen(navController = navController, vm = vm)
        }
    }
}
