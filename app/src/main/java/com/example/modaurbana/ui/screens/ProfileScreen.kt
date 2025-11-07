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
import coil.compose.AsyncImage
import com.example.modaurbana.viewmodel.ProfileUiState
import com.example.modaurbana.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Cargar datos cuando abre
    LaunchedEffect(Unit) { viewModel.loadUser(1) } // cambia a /me cuando lo expongas

    // Launcher: seleccionar desde galería (funcional ya)
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateAvatar(uri)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌ Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser(1) }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)

                    // Avatar simple (usa Coil si lo tienes en gradle)
                    Box(contentAlignment = Alignment.Center) {
                        if (state.avatarUri != null) {
                            AsyncImage(
                                model = state.avatarUri,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .clickable { pickImageLauncher.launch("image/*") }
                            )
                        } else {
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .clickable { pickImageLauncher.launch("image/*") },
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {}
                        }
                    }

                    // Nombre
                    InfoCard(title = "Nombre", value = state.userName)

                    // Email
                    InfoCard(title = "Email", value = state.userEmail)

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { viewModel.loadUser(1) }) {
                            Text("Refrescar")
                        }
                        OutlinedButton(onClick = {
                            viewModel.logout {
                                // La navegación a Login la manejas desde Main/Navigation
                                // o podrías pasar un callback como hicimos antes.
                            }
                        }) {
                            Text("Cerrar sesión")
                        }
                    }

                    // TODO: Cámara
                    // - Añade permisos en Manifest (CAMERA + READ_MEDIA_IMAGES / READ_EXTERNAL_STORAGE)
                    // - Crea un FileProvider y un URI temporal
                    // - Usa ActivityResultContracts.TakePicture() y viewModel.updateAvatar(uri)
                }
            }
        }
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
