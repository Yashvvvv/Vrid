package app.recruit.vrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.recruit.vrid.ui.blog.BlogDetailScreen
import app.recruit.vrid.ui.blog.BlogListScreen
import app.recruit.vrid.ui.theme.VridTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.foundation.isSystemInDarkTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val isDarkMode = isSystemInDarkTheme()

            var darkModeState by remember { mutableStateOf(isDarkMode) }

            VridTheme(
                darkTheme = darkModeState
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "blog_list",
                        modifier = Modifier.animateContentSize()
                    ) {
                        composable("blog_list") {
                            BlogListScreen(
                                onPostClick = { post ->
                                    navController.navigate("blog_detail/${post.id}")
                                },
                                isDarkMode = darkModeState,
                                onDarkModeChange = { darkModeState = it }
                            )
                        }

                        composable(
                            route = "blog_detail/{postId}",
                            arguments = listOf(
                                navArgument("postId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.getInt("postId")
                            BlogDetailScreen(
                                postId = postId ?: return@composable,
                                onBackClick = { navController.popBackStack() },
                                isDarkMode = darkModeState,
                                onDarkModeChange = { darkModeState = it }
                            )
                        }
                    }
                }
            }
        }
    }
}