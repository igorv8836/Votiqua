package com.example.feature.profile.ui.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.maxBitmapSize
import coil3.size.Size
import com.skydoves.landscapist.coil3.CoilImage
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FileInputStream

fun getKtorInputFromFile(path: String): Pair<ByteReadChannel, String>? {
    val file = File(path)
    if (!file.exists()) return null
    val stream = FileInputStream(file)
    val ext = file.extension.ifBlank { "jpg" }
    return stream.toByteReadChannel() to ext
}

@Composable
actual fun ProfilePhoto(
    url: String?,
    viewModel: ProfileViewModel
) {
    val fileChooser = remember {
        FileDialog(null as Frame?, "Choose a photo", FileDialog.LOAD).apply {
            setFilenameFilter { _, name ->
                name.endsWith(".jpg", ignoreCase = true) || 
                name.endsWith(".jpeg", ignoreCase = true) || 
                name.endsWith(".png", ignoreCase = true) ||
                name.endsWith(".gif", ignoreCase = true)
            }
            isMultipleMode = false
        }
    }
    
    val openFileChooser = {
        fileChooser.isVisible = true
        val selectedFiles = fileChooser.files
        if (selectedFiles.isNotEmpty()) {
            val selectedFile = selectedFiles[0]
            val data = getKtorInputFromFile(selectedFile.absolutePath)
            viewModel.onEvent(ProfileEvent.ChangePhoto(data))
        }
    }

    if (url == null) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(color = Color.Gray, shape = CircleShape)
                .clickable {
                    openFileChooser()
                }
        )
    } else {
        val context = LocalPlatformContext.current
        CoilImage(
            imageRequest = {
                ImageRequest.Builder(context)
                    .data(url)
                    .size(Size.ORIGINAL)
                    .maxBitmapSize(Size(1000, 2400))
                    .crossfade(true)
                    .build()
            },
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(color = Color.Gray, shape = CircleShape)
                .clickable { openFileChooser() }
        )
    }
}