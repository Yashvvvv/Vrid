package app.recruit.vrid.ui.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.recruit.vrid.data.model.BlogPost
import app.recruit.vrid.data.repository.BlogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogDetailViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BlogDetailUiState())
    val uiState: StateFlow<BlogDetailUiState> = _uiState

    fun loadPost(postId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val post = repository.getPostById(postId)
                _uiState.update {
                    it.copy(
                        post = post,
                        isLoading = false,
                        error = if (post == null) "Post not found" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}

data class BlogDetailUiState(
    val post: BlogPost? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 