package com.example.modaurbana.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLoggedOut: () -> Unit = {} // opcional: navega a Login desde afuera si quieres
) {
    // Observar el estado
    val state by viewModel.uiState.collectAsState()

    // 📸 Launcher: Selección desde galería (Guía 13)
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateAvatar(uri) // EP3: imagen de perfil
    }

    // 📷 Launcher: Tomar foto con cámara (TODO integración completa según Guía 13/Tutorial)
    // - Requiere crear un URI temporal con FileProvider y ActivityResultContracts.TakePicture()
    // - Declara permisos en Manifest (READ_MEDIA_IMAGES/READ_EXTERNAL_STORAGE + CAMERA)
    // val cameraLauncher = rememberLauncherForActivityResult(
    //     contract = ActivityResultContracts.TakePicture()
    // ) { success: Boolean ->
    //     if (success) viewModel.updateAvatar(temporalCameraUri)
    // }

    // Cargar datos cuando la pantalla se abre
    LaunchedEffect(Unit) {
        viewModel.loadUser(1)  // ⚠️ Cambia el ID o usa /me cuando lo expongas en tu repo
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            // Estado: Cargando
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Estado: Error
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser(1) }) {
                        Text("Reintentar")
                    }
                }
            }

            // Estado: Datos cargados
            else -> {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Perfil de Usuario",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 👤 Avatar (EP3 + Guía 13)
                    AvatarCard(
                        avatarUri = state.avatarUri,
                        onPickFromGallery = { pickImageLauncher.launch("image/*") },
                        onTakePhoto = {
                            // TODO: Implementar cámara:
                            // 1) Crear URI temporal con FileProvider
                            // 2) Lanzar cameraLauncher.launch(temporalUri)
                            // 3) En onActivityResult, si success -> viewModel.updateAvatar(temporalUri)
                        }
                    )

                    // Nombre
                    InfoCard(
                        title = "Nombre",
                        value = state.userName
                    )

                    // Email
                    InfoCard(
                        title = "Email",
                        value = state.userEmail
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { viewModel.loadUser(1) }) {
                            Text("Refrescar")
                        }
                        OutlinedButton(
                            onClick = { viewModel.logout(onLoggedOut) }
                        ) {
                            Text("Cerrar sesión")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta con acciones para avatar:
 * - Ver imagen actual (si existe) o placeholder
 * - Botones: "Elegir de galería" y "Tomar foto"
 * Completa la parte de cámara según tu Guía 13/Tutorial.
 */
@Composable
private fun AvatarCard(
    avatarUri: Uri?,
    onPickFromGallery: () -> Unit,
    onTakePhoto: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Vista muy simple del avatar (puedes sustituir por tu ImagenInteligente)
            if (avatarUri != null) {
                // Si ya usas Coil en el proyecto, puedes reemplazar por AsyncImage
                // AsyncImage(model = avatarUri, contentDescription = "Avatar", modifier = Modifier.size(120.dp).clip(CircleShape))
                Text("Avatar seleccionado: $avatarUri", style = MaterialTheme.typography.bodySmall)
            } else {
                Text("Sin avatar", style = MaterialTheme.typography.bodySmall)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onPickFromGallery) { Text("Elegir de galería") }
                Button(onClick = onTakePhoto) { Text("Tomar foto") }
            }
        }
    }
}

/**
 * Tarjeta reutilizable para mostrar pares título/valor del perfil.
 */
@Composable
private fun InfoCard(
    title: String,
    value: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
