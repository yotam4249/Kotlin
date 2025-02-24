package com.example.kotlinproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")

data class Post (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val rating: Float,
    var userName: String = "",
    var avatarUrl: String = "",
    var shoeDescription: String? = "",
    var shoePrice: String = "",
    var shoeUrl: String? = "",
    var likeUrl: String? = "",
    var category: String? = "",
    var lastUpdated: Long? = null

)