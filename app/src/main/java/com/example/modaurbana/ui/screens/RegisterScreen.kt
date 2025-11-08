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
fun RegisterScreen(nav: NavHostController, vm: AuthViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()

    LaunchedEffect(ui.loggedUser) {
        if (ui.loggedUser != null) {
            nav.navigate(Route.Home.route) {
                popUpTo(Route.Login.route) { inclusive = true }
            }
        }
    }


    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = ui.name, onValueChange = vm::onName, label = { Text("Nombre") },
            isError = ui.nameError != null, supportingText = { ui.nameError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ui.email, onValueChange = vm::onEmail, label = { Text("Email") },
            isError = ui.emailError != null, supportingText = { ui.emailError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ui.password, onValueChange = vm::onPass, label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = ui.passError != null, supportingText = { ui.passError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ui.confirm, onValueChange = vm::onConfirm, label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = ui.confirmError != null, supportingText = { ui.confirmError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        if (ui.errorMessage != null) Text(ui.errorMessage!!, color = MaterialTheme.colorScheme.error)

        Button(
            onClick = { vm.doRegister { /* navegación en LaunchedEffect */ } },
            enabled = !ui.loading,
            modifier = Modifier.fillMaxWidth()
        ) { if (ui.loading) CircularProgressIndicator(strokeWidth = 2.dp) else Text("Registrarme") }

        TextButton(onClick = { nav.navigate(Route.Login.route) }) { Text("Ya tengo cuenta") }
    }
}
