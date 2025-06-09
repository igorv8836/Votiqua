package com.example.feature.voting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.feature.voting.ui.manage_poll_screen.ManagePollScreen
import com.example.feature.voting.ui.manage_poll_screen.ManagePollViewModel
import com.example.feature.voting.ui.poll_list_screen.PollListScreen
import com.example.feature.voting.ui.poll_viewer_screen.PollViewerScreen
import com.example.feature.voting.ui.poll_viewer_screen.PollViewerViewModel
import com.example.votiqua.core.ui_common.navigation.ManagePollRoute
import com.example.votiqua.core.ui_common.navigation.MyPollsRoute
import com.example.votiqua.core.ui_common.navigation.PollCreateRoute
import com.example.votiqua.core.ui_common.navigation.PollViewerRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

internal class VotingNavigatorImpl : VotingNavigator {
    override fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    ) {
        navGraphBuilder.apply {
            composable<ManagePollRoute> {
                val pollId = it.toRoute<ManagePollRoute>().pollId ?: -1
                val viewModel: ManagePollViewModel = koinViewModel(
                    parameters = {
                        parametersOf(pollId)
                    },
                )

                ManagePollScreen(
                    isCreating = false,
                    viewModel = viewModel,
                    onClose = {
                        mainNavController.popBackStack()
                    },
                    onDeleted = {
                        mainNavController.popBackStack()
                    }
                )
            }

            composable<PollCreateRoute> {
                ManagePollScreen(
                    isCreating = true,
                    viewModel = koinViewModel(
                        parameters = { parametersOf(null) }
                    ),
                    onClose = {
                        mainNavController.popBackStack()
                    },
                    onDeleted = {
                        mainNavController.popBackStack()
                    }
                )
            }

            composable<PollViewerRoute>(
                deepLinks = listOf(
                    navDeepLink { uriPattern = "votiqua://poll/{pollId}" },
                    navDeepLink { uriPattern = "https://votiqua.quickqueues.tech/poll-link/{pollId}" }
                )
            ) {
                val pollId = it.toRoute<PollViewerRoute>().pollId
                val viewModel: PollViewerViewModel = koinViewModel(
                    parameters = {
                        parametersOf(pollId)
                    },
                )

                PollViewerScreen(
                    navController = mainNavController,
                    viewModel = viewModel,
                    onClose = {
                        mainNavController.popBackStack()
                    },
                )
            }

            composable<MyPollsRoute> {
                PollListScreen(
                    navController = mainNavController
                )
            }
        }
    }
}