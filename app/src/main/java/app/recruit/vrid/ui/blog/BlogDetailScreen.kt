package app.recruit.vrid.ui.blog

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import app.recruit.vrid.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(
    postId: Int,
    onBackClick: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BlogDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var fontSize by remember { mutableStateOf(16) }
    val context = LocalContext.current

    LaunchedEffect(postId) {
        viewModel.loadPost(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.post?.titleText ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onDarkModeChange(!isDarkMode) }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Light mode" else "Dark mode"
                        )
                    }
                    IconButton(onClick = { fontSize = (fontSize + 2).coerceAtMost(24) }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase font size")
                    }
                    IconButton(onClick = { fontSize = (fontSize - 2).coerceAtLeast(12) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease font size")
                    }
                    IconButton(onClick = {
                        uiState.post?.let { post ->
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, post.titleText)
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out this article: ${post.titleText}\nhttps://blog.vrid.in/?p=${post.id}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error ?: "Unknown error occurred",
                        onRetry = { viewModel.loadPost(postId) }
                    )
                }
                uiState.post != null -> {
                    val webViewContent = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1">
                            <style>
                                body {
                                    font-family: system-ui;
                                    padding: 16px;
                                    margin: 0;
                                    line-height: 1.6;
                                    font-size: ${fontSize}px;
                                    color: ${if (isDarkMode) "#E1E1E1" else "#1A1A1A"};
                                    background-color: ${if (isDarkMode) "#1A1A1A" else "#FFFFFF"};
                                }
                                img {
                                    max-width: 100%;
                                    height: auto;
                                    border-radius: 8px;
                                }
                            </style>
                        </head>
                        <body>
                            ${uiState.post!!.contentText}
                        </body>
                        </html>
                    """.trimIndent()

                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                settings.apply {
                                    javaScriptEnabled = true
                                    loadWithOverviewMode = true
                                    useWideViewPort = true
                                    domStorageEnabled = true
                                }
                            }
                        },
                        update = { webView ->
                            webView.loadDataWithBaseURL(
                                null,
                                webViewContent,
                                "text/html",
                                "UTF-8",
                                null
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
        }
    }
} 