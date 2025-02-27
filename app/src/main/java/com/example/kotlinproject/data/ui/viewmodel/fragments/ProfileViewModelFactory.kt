package com.example.kotlinproject.data.ui.viewmodel.fragments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostRepository
import com.example.kotlinproject.data.ui.viewmodel.fragments.UserRepository

class ProfileViewModelFactory(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(postRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
