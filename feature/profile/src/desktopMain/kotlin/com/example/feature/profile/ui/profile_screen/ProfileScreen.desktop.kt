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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.crossfade
import coil3.util.DebugLogger

@Composable
actual fun ProfilePhoto(url: String?) {
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

                }
        )
    } else {
//        setSingletonImageLoaderFactory {
//            getAsyncImageLoader(it)
//        }
        AsyncImage(
            model = url,
//            contentScale = ContentScale.Crop,
            contentDescription = "",
//            alignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(color = Color.Gray, shape = CircleShape)
                .clickable {

                }
        )
    }
}

fun getAsyncImageLoader(context: PlatformContext)=
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()
