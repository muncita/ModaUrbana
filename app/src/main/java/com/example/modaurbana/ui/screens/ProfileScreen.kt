package com.example.modaurbana.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.viewmodel.ProfileUiState
import com.example.modaurbana.viewmodel.ProfileViewModel
import coil.compose.AsyncImage
import com.example.modaurbana.ui.components.ImagenInteligente

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLoggedOut: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.updateAvatar(uri) }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            state.error != null -> Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
            else -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
                ImagenInteligente(uri = state.avatarUri) { showDialog = true }

                InfoCard("Nombre", state.userName)
                InfoCard("Email", state.userEmail)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.loadUser() }) { Text("Refrescar") }
                    OutlinedButton(onClick = { viewModel.logout(onLoggedOut) }) { Text("Cerrar sesión") }
                }
            }
        }
    }

    if (showDialog) {
        ImagePickerDialog(
            onDismiss = { showDialog = false },
            onCameraClick = { /* TODO: cámara / showDialog = false },
            onGalleryClick = { pickImageLauncher.launch("image/"); showDialog = false }
        )
    }
}

@Composable
private fun InfoCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
