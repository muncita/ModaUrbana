package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

/**
 * Registro con validaciones por campo (Guía 11) y navegación a Login si success.
 * Si tu API no soporta /register, deja visible la pantalla como formulario demostrativo
 * (EP3 mide validaciones/UI/estados aunque el endpoint sea opcional).
 */
@Composable
fun RegisterScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { vm.register(username, email, password) /* TODO: o navegar directo a login si no hay /register */ },
            enabled = !ui.isLoading /* && validaciones cumplidas */
        ) { Text("Crear cuenta") }

        TextButton(onClick = { navController.navigate(Route.Login.path) }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }

        if (ui.success) {
            LaunchedEffect(true) {
                navController.navigate(Route.Login.path) { popUpTo(Route.Register.path) { inclusive = true } }
            }
        }
    }
}
