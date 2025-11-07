package com.example.modaurbanaprototipoapp.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.modaurbanaprototipoapp.viewmodel.ProfileViewModel
import com.example.modaurbanaprototipoapp.viewmodel.ProfileUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Int,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) { viewModel.loadCurrentUserProfile() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moda Urbana SPA") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Text(state.error ?: "", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCurrentUserProfile() }) { Text("Reintentar") }
                    }
                }
                state.user != null -> {
                    ProfileContent(state = state, onLogout = onLogout, viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val user = state.user ?: return
    val context = LocalContext.current
    var showImagePicker by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val permissionsState = rememberMultiplePermissionsState(permissions)

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            viewModel.updateAvatar(tempCameraUri)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateAvatar(it)
        }
    }

    if (showImagePicker) {
        ImagePickerDialog(
            onDismiss = { showImagePicker = false },
            onCameraClick = {
                showImagePicker = false
                val cameraPermission = permissionsState.permissions.find {
                    it.permission == Manifest.permission.CAMERA
                }
                if (cameraPermission?.status?.isGranted == true) {
                    tempCameraUri = createImageUri(context)
                    tempCameraUri?.let { takePictureLauncher.launch(it) }
                } else {
                    permissionsState.launchMultiplePermissionRequest()
                }
            },
            onGalleryClick = {
                showImagePicker = false
                val imagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

                if (permissionsState.permissions.any {
                        it.permission == imagePermission && it.status.isGranted  //
                    }) {
                    pickImageLauncher.launch("image/*")
                } else {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (state.avatarUri != null) {
                AsyncImage(
                    model = state.avatarUri,
                    contentDescription = "Avatar del usuario",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable { showImagePicker = true },
                    contentScale = ContentScale.Crop
                )
            } else if (!user.image.isNullOrEmpty()) {
                AsyncImage(
                    model = user.image,
                    contentDescription = "Avatar de ${user.name}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable { showImagePicker = true },
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showImagePicker = true },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Seleccionar avatar",
                        modifier = Modifier.padding(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { showImagePicker = true },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Cambiar foto",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(text = user.name ?: "Usuario", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        Text(text = user.email, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)

        Spacer(Modifier.height(24.dp))

        Card(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Correo Electrónico", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(user.email, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                HorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "ID", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("ID de Usuario", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(user.id.toString(), style = MaterialTheme.typography.bodyLarge)
                    }
                }

                if (user.createdAt != null) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = "Registro", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Miembro desde", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatDate(user.createdAt), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar foto") },
        text = { Text("Elige una opción para cambiar tu foto de perfil") },
        confirmButton = {
            TextButton(onClick = onCameraClick) {
                Text("Cámara")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onGalleryClick) {
                    Text("Galería")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

private fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "profile_avatar_$timeStamp.jpg"
    val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)

    return try {
        val imageFile = File(storageDir, imageFileName)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    } catch (e: Exception) {
        null
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}