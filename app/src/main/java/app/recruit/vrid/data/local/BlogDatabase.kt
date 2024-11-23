package app.recruit.vrid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.recruit.vrid.data.model.BlogPost

@Database(entities = [BlogPost::class], version = 1)
@TypeConverters(Converters::class)
abstract class BlogDatabase : RoomDatabase() {
    abstract fun blogDao(): BlogDao
} 