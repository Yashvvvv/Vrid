package app.recruit.vrid.ui.blog

import app.recruit.vrid.data.model.BlogPost

data class BlogUiState(
    val posts: List<BlogPost> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
) 