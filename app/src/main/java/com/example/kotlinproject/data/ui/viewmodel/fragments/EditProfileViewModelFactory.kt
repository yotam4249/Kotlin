package com.example.kotlinproject.data.ui.viewmodel.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinproject.data.ImageHandler

class EditProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val imageHandler: ImageHandler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(userRepository, imageHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
