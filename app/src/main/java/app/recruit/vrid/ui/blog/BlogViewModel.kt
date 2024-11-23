package app.recruit.vrid.ui.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.recruit.vrid.data.repository.BlogRepository
import app.recruit.vrid.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repository: BlogRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState: StateFlow<BlogUiState> = _uiState

    val isOffline = networkMonitor.isOnline
        .map { !it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        loadPosts()
        observePosts()
    }

    private fun observePosts() {
        viewModelScope.launch {
            repository.getAllPosts()
                .catch { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
                .collect { posts ->
                    _uiState.update { 
                        it.copy(
                            posts = posts,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val result = repository.refreshPosts(page = _uiState.value.currentPage)
                result.fold(
                    onSuccess = { posts ->
                        _uiState.update { 
                            it.copy(
                                currentPage = it.currentPage + 1,
                                isLoading = false,
                                hasMorePages = posts.isNotEmpty()
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                error = error.message ?: "Unknown error occurred",
                                isLoading = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Unknown error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun retryLoadingPosts() {
        loadPosts()
    }

    fun loadNextPage() {
        if (!_uiState.value.isLoading && _uiState.value.hasMorePages) {
            loadPosts()
        }
    }

    fun refresh() {
        _uiState.update { it.copy(currentPage = 1, hasMorePages = true) }
        loadPosts()
    }
} 