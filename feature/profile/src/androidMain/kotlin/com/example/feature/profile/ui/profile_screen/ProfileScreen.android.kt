package com.example.feature.profile.ui.profile_screen

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.maxBitmapSize
import coil3.size.Size
import com.skydoves.landscapist.coil3.CoilImage
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import kotlinx.coroutines.launch

fun getKtorInputFromUri(context: Context, uriString: String): Pair<ByteReadChannel, String>? {
    val uri = uriString.toUri()
    val cr = context.contentResolver
    val stream = cr.openInputStream(uri) ?: return null
    val ext = cr.getType(uri)?.substringAfterLast("/") ?: "jpg"
    return stream.toByteReadChannel() to ext
}

@Composable
actual fun ProfilePhoto(
    url: String?,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val input = getKtorInputFromUri(context, it.toString())
                scope.launch {
                    viewModel.onEvent(ProfileEvent.ChangePhoto(input))
                }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                pickImageLauncher.launch("image/*")
            }
        }
    )

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
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    )
                }
        )
    } else {
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
                .clickable {
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    )
                }
        )
    }
}