package com.example.kotlinproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinproject.data.model.PostLike

@Dao
interface PostLikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(postLike: PostLike): Long

    @Query("SELECT COUNT(*) FROM post_likes WHERE postId = :postId")
    suspend fun getLikeCount(postId: Int): Int

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasUserLiked(postId: Int, userId: String): Boolean

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deleteLike(postId: Int, userId: String)

}
