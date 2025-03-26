package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinproject.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    val email = MutableLiveData("")
    val username = MutableLiveData("")
    val avatarUrl = MutableLiveData("")

    val isLoading = MutableLiveData(false)

    fun fetchUserProfile() {
        isLoading.value = true
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedUser = userRepository.getUserById(userId)
                withContext(Dispatchers.Main) {
                    _user.value = fetchedUser
                    if (fetchedUser != null) {
                        email.value = fetchedUser.email
                        username.value = fetchedUser.name
                        avatarUrl.value = fetchedUser.photoUrl
                    }
                }
            } catch (e: Exception) {
                Log.e("EditProfileViewModel", "Error fetching user profile", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    /**
     * Update user profile details
     */
    fun updateUserProfile(onSuccess: () -> Unit, onFailure: (Exception?) -> Unit) {
        isLoading.value = true
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("User not logged in"))
            isLoading.value = false
            return
        }

        val updatedUser = User(
            uid = userId,
            name = username.value ?: "",
            email = email.value ?: "",
            photoUrl = avatarUrl.value ?: ""
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.updateUser(updatedUser)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("EditProfileViewModel", "Error updating profile", e)
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    /**
     * Update avatar image URL
     */
    fun updateAvatar(newUri: Uri) {
        avatarUrl.value = newUri.toString()
    }
}
