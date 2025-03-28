package com.example.kotlinproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinproject.data.dao.*
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.model.PostLike


@Database(entities = [Post::class, PostLike::class], version = 3)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postLikeDao(): PostLikeDao

    companion object {
        @Volatile private var INSTANCE: PostDatabase? = null

        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "post_database"
                )
                    .fallbackToDestructiveMigration() // allow migration for now
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
