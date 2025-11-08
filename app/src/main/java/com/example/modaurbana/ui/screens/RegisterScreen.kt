package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavHostController, vm: AuthViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navegar si se registró correctamente
    LaunchedEffect(ui.success) {
        if (ui.success) {
            navController.navigate(Route.Home.route) {
                popUpTo(Route.Register.route) { inclusive = true }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        ui.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { vm.doRegister(name, email, password) },
            enabled = !ui.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (ui.isLoading) CircularProgressIndicator(strokeWidth = 2.dp)
            else Text("Registrarme")
        }

        TextButton(onClick = { navController.navigate(Route.Login.route) }) {
            Text("Ya tengo cuenta")
        }
    }
}
