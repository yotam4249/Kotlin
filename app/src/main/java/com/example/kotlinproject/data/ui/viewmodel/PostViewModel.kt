package com.example.kotlinproject.data.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.model.Post


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val db = PostDatabase.getDatabase(application)
    private val postDao = db.postDao()

    fun addPost(post: Post) {
        viewModelScope.launch {
            postDao.insert(post)
        }
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        viewModelScope.launch {
            callback(postDao.getAllPosts())
        }
    }
}
