package org.example.votiqua

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import com.example.votiqua.core.ui_common.navigation.PollViewerRoute
import com.example.votiqua.core.ui_common.navigation.ScreenRoute

class MainActivity : ComponentActivity() {
    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRoute = parseRouteFromIntent(intent)
        setContent {
            MyApp(startRoute = startRoute) { controller ->
                navController = controller
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val route = parseRouteFromIntent(intent)
        route?.let { navController?.navigate(it) }
    }

    private fun parseRouteFromIntent(intent: Intent?): ScreenRoute? {
        val data = intent?.data ?: return null

        return when {
            data.scheme == "votiqua" && data.host == "poll" -> {
                data.lastPathSegment?.toIntOrNull()?.let { PollViewerRoute(it) }
            }
            data.scheme == "https" && data.host == "votiqua.quickqueues.tech" &&
                data.pathSegments.firstOrNull() == "poll-link" -> {
                data.lastPathSegment?.toIntOrNull()?.let { PollViewerRoute(it) }
            }
            else -> null
        }
    }
}