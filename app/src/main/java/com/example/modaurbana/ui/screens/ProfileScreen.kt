package com.example.modaurbana.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.modaurbana.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileScreen(pvm: ProfileViewModel = viewModel()) {
    val avatar by pvm.avatarUri.collectAsState()
    val ctx = LocalContext.current

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { pvm.setAvatar(it.toString()) }
    }

    // Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) cameraUri?.let { pvm.setAvatar(it.toString()) }
    }

    // Permiso cámara
    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val (uri, _) = createImageUri(ctx)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineSmall)

        if (avatar.isNotEmpty()) {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                modifier = Modifier.size(128.dp)
            )
        } else {
            Text("Sin imagen de perfil")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) { Text("Desde galería") }

            Button(onClick = {
                val status = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
                if (status == PackageManager.PERMISSION_GRANTED) {
                    val (uri, _) = createImageUri(ctx)
                    cameraUri = uri
                    cameraLauncher.launch(uri)
                } else {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
            }) { Text("Tomar foto") }
        }

        Button(
            onClick = { pvm.clearAvatar() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Eliminar imagen")
        }
    }
}

/** Crea un archivo temporal y devuelve su Uri (FileProvider) + el File */
private fun createImageUri(context: Context): Pair<Uri, File> {
    val imagesDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
    val file = File.createTempFile("avatar_", ".jpg", imagesDir)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    return uri to file
}
