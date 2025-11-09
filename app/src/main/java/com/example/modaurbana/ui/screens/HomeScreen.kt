package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modaurbana.viewmodel.AuthViewModel
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route

@Composable
fun HomeScreen(navController: NavHostController, vm: AuthViewModel) {
    val ui by vm.ui.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadUser()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            ui.isLoading -> CircularProgressIndicator()

            ui.error != null -> Text("Error: ${ui.error}")

            ui.user != null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Bienvenido, ${ui.user?.name}")
                Spacer(Modifier.height(8.dp))
                Text("Correo: ${ui.user?.email}")
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        vm.logout()
                        // Navega limpiando el backstack para evitar volver con back
                        navController.navigate(Route.Login.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
