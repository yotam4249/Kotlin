package com.example.kotlinproject.data.dao

import androidx.room.*
import com.example.kotlinproject.data.model.Post


@Dao
interface postDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts ORDER BY rating DESC")
    suspend fun getAllPosts(): List<Post>

    @Delete
    suspend fun delete(post: Post)
}

