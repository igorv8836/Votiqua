package org.example.votiqua.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.common.SnackbarManager
import com.example.feature.auth.navigation.AuthNavigator
import com.example.feature.auth.navigation.RecomNavigator
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.voting.navigation.VotingNavigator
import com.example.votiqua.core.ui_common.navigation.SplashRoute
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import votiqua.core.ui_common.generated.resources.Res
import votiqua.core.ui_common.generated.resources.basic_error
import com.example.common.SnackbarDuration as MySnackbarDuration

@Composable
fun AppNavigation(navController: NavHostController) {
    val authNavigator: AuthNavigator = koinInject<AuthNavigator>()
    val profileNavigator: ProfileNavigator = koinInject<ProfileNavigator>()
    val votingNavigator: VotingNavigator = koinInject<VotingNavigator>()
    val recomNavigator: RecomNavigator = koinInject<RecomNavigator>()
    val snackbarManager: SnackbarManager = koinInject<SnackbarManager>()

    val basicErrorText = stringResource(Res.string.basic_error)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        snackbarManager.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message.message ?: basicErrorText,
                duration = when (message.duration) {
                    MySnackbarDuration.Short -> SnackbarDuration.Short
                    MySnackbarDuration.Long -> SnackbarDuration.Long
                    MySnackbarDuration.Indefinite -> SnackbarDuration.Indefinite
                }
            )
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier
                .padding(innerPadding)
                .systemBarsPadding()
                .navigationBarsPadding()
        ) {
            authNavigator.registerNavigation(this, navController)
            profileNavigator.registerNavigation(this, navController)
            votingNavigator.registerNavigation(this, navController)
            recomNavigator.registerNavigation(this, navController)
        }
    }
}