package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlinproject.data.dao.PostDao
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.Post

class PostRepository(context: Context) {
    private val postDao: PostDao = PostDatabase.getDatabase(context).postDao()

    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getUserPosts(null.toString()) ?: throw Exception("No Posts Found")
    }

    fun getPostsByUser(username: String): LiveData<List<Post>> {
        return postDao.getUserPosts(username)
    }

    fun getPostById(postId: String): LiveData<Post?>? {
        return postDao.getPostById(postId)
    }

    suspend fun savePost(post: Post) {
        postDao.insert(post)
    }

    suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }
}
