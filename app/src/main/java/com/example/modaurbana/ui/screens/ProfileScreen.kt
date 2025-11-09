package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavHostController, vm: AuthViewModel) {
    val ui by vm.ui.collectAsState()

    // Si no hay usuario logueado, redirigir al login
    LaunchedEffect(ui.user) {
        if (ui.user == null && ui.error == null) {
            navController.navigate(Route.Login.route) {
                popUpTo(Route.Profile.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            ui.isLoading -> CircularProgressIndicator()
            ui.user != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("👤 Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("Nombre: ${ui.user?.name}", fontWeight = FontWeight.Bold)
                    Text("Correo: ${ui.user?.email}")
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            vm.logout()
                            navController.navigate(Route.Login.route) {
                                popUpTo(0) // Limpia el backstack
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
            ui.error != null -> {
                Text("Error: ${ui.error}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
