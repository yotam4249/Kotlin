package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostRepository
import com.example.kotlinproject.data.ui.viewmodel.fragments.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts
    fun getPostsByUserId(userId: String): LiveData<List<Post>> {
        return postRepository.getPostsByUserId(userId)
    }
    fun getPostsByUser(username: String): LiveData<List<Post>> {
        return postRepository.getPostsByUser(username)
    }
    fun deletePost(post: Post) {
        viewModelScope.launch {
            postRepository.deletePost(post)
        }
    }




    val isLoading = MutableLiveData(false)

    /**
     * Fetch user data and posts
     */
    fun fetchProfile() {
        isLoading.value = true
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.e("ProfileViewModel", "User not logged in")
            return
        }


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedUser = userRepository.getUserById(userId)
                val userPosts = postRepository.getPostsByUser(userId)?.value




                withContext(Dispatchers.Main) {
                    if (userPosts != null) {
                        _user.value = fetchedUser
                        _posts.value = userPosts.filterNotNull()
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile data", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    /**
     * Log out the user
     */
    fun logOut(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}
