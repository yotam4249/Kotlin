package com.example.kotlinproject.data.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postDao = PostDatabase.getDatabase(application).postDao()
    private val postLikeDao = PostDatabase.getDatabase(application).postLikeDao()
    private val repository = PostRepository(application)

    val allPosts: LiveData<List<Post>> = postDao.getAllPosts()

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postDao.insert(post)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            repository.updatePost(post)
        }
    }


    fun likePost(postId: Int, userId: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.toggleLike(postId, userId)
            withContext(Dispatchers.Main) {
                onSuccess(count)
            }
        }
    }


    fun deletePost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            postDao.delete(post)
        }
    }
}

