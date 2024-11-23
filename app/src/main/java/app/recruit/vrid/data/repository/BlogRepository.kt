package app.recruit.vrid.data.repository

import app.recruit.vrid.data.api.BlogApi
import app.recruit.vrid.data.local.BlogDao
import app.recruit.vrid.data.model.BlogPost
import app.recruit.vrid.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepository @Inject constructor(
    private val api: BlogApi,
    private val dao: BlogDao,
    private val networkMonitor: NetworkMonitor
) {
    fun getAllPosts(): Flow<List<BlogPost>> = dao.getAllPosts()

    suspend fun refreshPosts(page: Int = 1): Result<List<BlogPost>> {
        return try {
            // Check network connectivity
            if (networkMonitor.isOnline.first()) {
                val posts = api.getPosts(page = page)
                if (page == 1) {
                    dao.clearAllPosts()
                }
                dao.insertPosts(posts)
                Result.success(posts)
            } else {
                // If offline, return cached data for first page
                if (page == 1) {
                    val cachedPosts = dao.getAllPosts().first()
                    if (cachedPosts.isNotEmpty()) {
                        Result.success(cachedPosts)
                    } else {
                        Result.failure(Exception("No internet connection and no cached data available"))
                    }
                } else {
                    Result.failure(Exception("No internet connection"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPostById(id: Int): BlogPost? = dao.getPostById(id)
} 