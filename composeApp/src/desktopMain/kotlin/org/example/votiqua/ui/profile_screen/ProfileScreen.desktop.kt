package org.example.votiqua.ui.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
actual fun ProfilePhoto(
    url: String?,
) {
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
        CoilImage(
            imageModel = { url },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ),
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(color = Color.Gray, shape = CircleShape)
                .clickable {

                }
        )
    }
}