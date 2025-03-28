package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlinproject.data.dao.PostDao
import com.example.kotlinproject.data.dao.PostLikeDao
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.model.PostLike



class PostRepository(context: Context) {
    private val postDao: PostDao = PostDatabase.getDatabase(context).postDao()
    private val postLikeDao: PostLikeDao = PostDatabase.getDatabase(context).postLikeDao()

    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getAllPosts()
    }

    suspend fun toggleLike(postId: Int, userId: String): Int {
        val hasLiked = postLikeDao.hasUserLiked(postId, userId)
        if (hasLiked) {
            postLikeDao.deleteLike(postId, userId)
        } else {
            postLikeDao.insertLike(PostLike(postId, userId))
        }

        val count = postLikeDao.getLikeCount(postId)
        postDao.updateLikeCount(postId, count)
        return count
    }

    suspend fun getLikeCount(postId: Int): Int {
        return postLikeDao.getLikeCount(postId)
    }

    suspend fun hasUserLiked(postId: Int, userId: String): Boolean {
        return postLikeDao.hasUserLiked(postId, userId)
    }

    fun getPostsByUser(username: String): LiveData<List<Post>> {
        return postDao.getUserPosts(username)
    }

    fun getPostById(postId: String): LiveData<Post?>? {
        return postDao.getPostById(postId)
    }
    fun getPostsByCategory(category: String): LiveData<List<Post>> {
        return postDao.getPostsByCategory(category)
    }

    fun getPostsByBrand(brand: String): LiveData<List<Post>> {
        return postDao.getPostsByBrand(brand)
    }

    fun getPostsByPrice(maxPrice: Double): LiveData<List<Post>> {
        return postDao.getPostsByPrice(maxPrice)
    }

    fun getPostsByRating(minRating: Float): LiveData<List<Post>> {
        return postDao.getPostsByRating(minRating)
    }

    suspend fun savePost(post: Post) {
        postDao.insert(post)
    }

    suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }
    suspend fun updatePost(post: Post) {
        postDao.updatePost(post)
    }
}
