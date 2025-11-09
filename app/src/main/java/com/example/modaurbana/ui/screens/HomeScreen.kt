package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.viewmodel.AuthViewModel
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController, vm: AuthViewModel) {
    val ui by vm.ui.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadUser()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
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
                Button(onClick = { vm.logout() }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
