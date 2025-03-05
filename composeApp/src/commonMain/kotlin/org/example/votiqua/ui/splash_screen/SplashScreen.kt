package org.example.votiqua.ui.splash_screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orbit_mvi.compose.collectAsState
import org.example.votiqua.ui.navigation.navigateToLogin
import org.example.votiqua.ui.navigation.navigateToMain
import org.koin.compose.viewmodel.koinViewModel


@Composable
internal fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    SplashScreen(
        state,
        navigateToMain = navController::navigateToMain,
        navigateToLogin = navController::navigateToLogin,
        viewModel::onEvent
    )
}

@Composable
internal fun SplashScreen(
    state: SplashState,
    navigateToMain: () -> Unit,
    navigateToLogin: () -> Unit,
    onEvent: (SplashEvent) -> Unit
) {
    when (state) {
        is SplashState.Loading -> {
            SplashLoading()
        }

        is SplashState.Success -> {
            LaunchedEffect(Unit) {
                navigateToMain()
            }
        }

        is SplashState.Error -> {
            SplashError(
                message = state.message,
                onRetry = {
                    onEvent(SplashEvent.Retry)
                })
        }

        SplashState.Unauthorized -> {
            LaunchedEffect(Unit) {
                navigateToLogin()
            }
        }
    }
}

@Composable
internal fun SplashLoading() {
    val color = MaterialTheme.colorScheme.primary
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            drawCircle(
                color = color,
                radius = size.minDimension / 2 * scale
            )
        }
    }
}


@Composable
fun SplashError(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Повторить", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}