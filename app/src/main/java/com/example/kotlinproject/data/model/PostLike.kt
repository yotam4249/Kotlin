package com.example.kotlinproject.data.model

import androidx.room.Entity

@Entity(tableName = "post_likes", primaryKeys = ["postId", "userId"])
data class PostLike(
    val postId: Int,
    val userId: String
)
