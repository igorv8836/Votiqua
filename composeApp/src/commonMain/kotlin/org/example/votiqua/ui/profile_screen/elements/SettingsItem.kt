package org.example.votiqua.ui.profile_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.votiqua.ui.common.Dimens

@Composable
fun SettingsItem(
    text: String,
    isLast: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.primary,
    click: () -> Unit = {},
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(color = textColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { click() }
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        textAlign = TextAlign.Left
    )
    if (!isLast) {
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(start = Dimens.small)
        )
    }

}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = Dimens.small)
    )
}