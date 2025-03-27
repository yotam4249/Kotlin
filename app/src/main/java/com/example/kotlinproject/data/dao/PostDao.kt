package com.example.kotlinproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinproject.data.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts ORDER BY rating DESC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE user_name = :username")
    fun getUserPosts(username: String): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE user_name = :postUserName")
    fun getPostByUserName(postUserName: String?): Post?

    @Query("SELECT * FROM posts WHERE id=:id")
    fun getPostById(id: String?): LiveData<Post?>?

    @Query("SELECT * FROM posts WHERE id IN (:postIds)")
    fun getLikedPosts(postIds: List<String?>?): List<Post?>?

    @Query("SELECT * FROM posts WHERE name LIKE '%' || :searchString || '%'")
    fun getPostsByShoeName(searchString: String?): List<Post?>?

    // ✅ clearly fixed query
    @Query("UPDATE posts SET user_name = :newName, avatar_url = :newAvatarUrl WHERE userId = :userId")
    suspend fun updateUserDetailsInPosts(userId: String, newName: String, newAvatarUrl: String)

    @Delete
    suspend fun delete(post: Post)
}
