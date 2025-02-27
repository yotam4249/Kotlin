package com.example.kotlinproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinproject.data.model.Post


@Dao
interface postDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts ORDER BY rating DESC")
    suspend fun getAllPosts(): List<Post>

    @Query("select * from posts where  user_name= :username")
    fun getUserPosts(username: String?): LiveData<List<Post?>?>?

    @Query("select * from posts where user_name = :postUserName")
    fun getPostByUserName(postUserName: String?): Post?

    @Query("select * from posts where id=:id")
    fun getPostById(id: String?): LiveData<Post?>?

    @Query("select * from posts where id in (:postIds)")
    fun getLikedPosts(postIds: List<String?>?): List<Post?>?

    @Query("SELECT * FROM posts WHERE name LIKE '%' || :searchString || '%'")
    fun getPostsByShoeName(searchString: String?): List<Post?>?


    @Delete
    suspend fun delete(post: Post)
}

