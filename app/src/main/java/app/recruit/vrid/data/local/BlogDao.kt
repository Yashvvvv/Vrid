package app.recruit.vrid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.recruit.vrid.data.model.BlogPost
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogDao {
    @Query("SELECT * FROM blog_posts ORDER BY date DESC")
    fun getAllPosts(): Flow<List<BlogPost>>

    @Query("SELECT * FROM blog_posts WHERE id = :id")
    suspend fun getPostById(id: Int): BlogPost?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<BlogPost>)

    @Query("DELETE FROM blog_posts")
    suspend fun clearAllPosts()
} 