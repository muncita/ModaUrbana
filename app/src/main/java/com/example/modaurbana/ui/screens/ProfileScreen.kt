package com.example.modaurbana.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.modaurbana.R
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    vm: AuthViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Avatar guardado en SessionManager (AuthViewModel)
    val avatar by vm.avatarUriFlow.collectAsState(initial = "")

    // Si no hay usuario logueado, mandar a login
    LaunchedEffect(ui.user) {
        if (ui.user == null && ui.error == null) {
            navController.navigate(Route.Login.route) { popUpTo(0) }
        }
    }

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // ----------- LAUNCHER GALERÍA -----------
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { picked: Uri? ->
        picked?.let {
            scope.launch { vm.saveAvatarUri(it.toString()) }
        }
    }

    // ----------- LAUNCHER CÁMARA (TakePicture con URI) -----------
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            tempCameraUri?.let { uri ->
                scope.launch { vm.saveAvatarUri(uri.toString()) }
            }
        }
    }

    fun createImageUriForCamera(context: Context): Uri {
        val imagesDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
        val file = File.createTempFile("avatar_", ".jpg", imagesDir)
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

    // ----------- PERMISO DE CÁMARA -----------
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUriForCamera(context).also { tempCameraUri = it }
            cameraLauncher.launch(uri)
        } else {
            // aquí podrías mostrar un Toast si quieres
        }
    }

    // Función que maneja todo el flujo de "Tomar foto"
    fun onTakePhotoClick() {
        val hasCamera = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasCamera) {
            val uri = createImageUriForCamera(context).also { tempCameraUri = it }
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // ------------------ UI ------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🟣 AVATAR
            ProfileAvatar(
                avatar = avatar,
                context = context
            )

            Spacer(Modifier.height(16.dp))

            // 🖼 Botones foto (misma funcionalidad que antes)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text("Desde galería")
                }

                OutlinedButton(
                    onClick = { onTakePhotoClick() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text("Tomar foto")
                }
            }

            Spacer(Modifier.height(24.dp))

            // 📄 Datos del usuario en una card
            ProfileInfoCard(
                nombre = ui.user?.name ?: "-",
                email = ui.user?.email ?: "-"
            )

            Spacer(Modifier.height(32.dp))

            // 🚪 Cerrar sesión (misma lógica que antes)
            Text(
                text = "¿Quieres cerrar tu sesión?",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    vm.logout()
                    navController.navigate(Route.Login.route) { popUpTo(0) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    avatar: String,
    context: Context
) {
    val hasAvatar = avatar.isNotBlank()

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (hasAvatar) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar por defecto",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileInfoCard(
    nombre: String,
    email: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Datos de tu cuenta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Nombre: $nombre",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Correo: $email",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
