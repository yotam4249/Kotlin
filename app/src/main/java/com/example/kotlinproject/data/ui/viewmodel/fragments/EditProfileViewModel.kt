
package com.example.kotlinproject.data.ui.viewmodel.fragments
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.dao.PostDao
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.data.ui.viewmodel.fragments.UserRepository
import kotlinx.coroutines.launch


//
//import android.net.Uri
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.kotlinproject.data.model.User
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class EditProfileViewModel(
//    private val userRepository: UserRepository
//) : ViewModel() {
//
////    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
////
////    private val _user = MutableLiveData<User>()
////    val user: LiveData<User> get() = _user
////
////    val email = MutableLiveData("")
////    val username = MutableLiveData("")
////    val avatarUrl = MutableLiveData("")
////
////    val isLoading = MutableLiveData(false)
////
////    fun fetchUserProfile() {
////        isLoading.value = true
////        val userId = auth.currentUser?.uid ?: return
////
////        viewModelScope.launch(Dispatchers.IO) {
////            try {
////                val fetchedUser = userRepository.getUserById(userId)
////                withContext(Dispatchers.Main) {
////                    _user.value = fetchedUser
////                    if (fetchedUser != null) {
////                        email.value = fetchedUser.email
////                        username.value = fetchedUser.name
////                        avatarUrl.value = fetchedUser.photoUrl
////                    }
////                }
////            } catch (e: Exception) {
////                Log.e("EditProfileViewModel", "Error fetching user profile", e)
////            } finally {
////                withContext(Dispatchers.Main) { isLoading.value = false }
////            }
////        }
////    }
////
////    /**
////     * Update user profile details
////     */
////    fun updateUserProfile(onSuccess: () -> Unit, onFailure: (Exception?) -> Unit) {
////        isLoading.value = true
////        val userId = auth.currentUser?.uid
////        if (userId == null) {
////            onFailure(Exception("User not logged in"))
////            isLoading.value = false
////            return
////        }
////
////        val updatedUser = User(
////            uid = userId,
////            name = username.value ?: "",
////            email = email.value ?: "",
////            photoUrl = avatarUrl.value ?: ""
////        )
////
////        viewModelScope.launch(Dispatchers.IO) {
////            try {
////                userRepository.updateUser(updatedUser)
////                withContext(Dispatchers.Main) {
////                    onSuccess()
////                }
////            } catch (e: Exception) {
////                Log.e("EditProfileViewModel", "Error updating profile", e)
////                withContext(Dispatchers.Main) {
////                    onFailure(e)
////                }
////            } finally {
////                withContext(Dispatchers.Main) { isLoading.value = false }
////            }
////        }
////    }
////
////    /**
////     * Update avatar image URL
////     */
////    fun updateAvatar(newUri: Uri) {
////        avatarUrl.value = newUri.toString()
////    }
////
//
//}

class EditProfileViewModel(
    private  val userRepository: UserRepository,
    private val imageStorageHandler : ImageHandler,
    private val postDao: PostDao,
    var newProfileImageUri: Uri? = null

):ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user
    val username = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)

    fun loadUser(userFromArgs: User) {
        _user.value = userFromArgs
        username.value = userFromArgs.name
    }

    fun updateImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            val path = imageStorageHandler.saveImageToStorage(context, uri)
            _user.value = _user.value?.copy(photoUrl = path)
        }
    }

    fun updateUserProfile(
        userId: String, // Clearly use userId
        imageUri: Uri?,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                var updatedPhotoUrl = _user.value?.photoUrl ?: ""

                if (imageUri != null) {
                    updatedPhotoUrl = imageStorageHandler.saveImageToStorage(context, imageUri).toString()
                }

                val updatedUser = _user.value?.copy(
                    name = username.value ?: "",
                    photoUrl = updatedPhotoUrl
                )

                if (updatedUser != null) {
                    userRepository.updateUser(updatedUser)
                    _user.postValue(updatedUser)

                    // ✅ Clearly update posts using userId
                    postDao.updateUserDetailsInPosts(
                        userId = userId,
                        newName = updatedUser.name,
                        newAvatarUrl = updatedUser.photoUrl ?: ""
                    )

                    val reloadedUser = userRepository.getUserByEmail(updatedUser.email)
                    _user.postValue(reloadedUser!!)
                    onSuccess()
                }
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                isLoading.value = false
            }
        }
    }

}
