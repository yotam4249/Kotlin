package com.example.kotlinproject.data.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.model.Post
import kotlinx.coroutines.Dispatchers


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postDao = PostDatabase.getDatabase(application).postDao()

    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postDao.insert(post)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postDao.delete(post)
        }
    }
}

