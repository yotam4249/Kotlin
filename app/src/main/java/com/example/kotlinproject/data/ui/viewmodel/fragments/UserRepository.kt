package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Context
import com.example.kotlinproject.data.dao.UserDao
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(context: Context) {
    private val db = FirebaseFirestore.getInstance()
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

    suspend fun getUserById(userId: String): User {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            snapshot.toObject(User::class.java) ?: throw Exception("User not found")
        } catch (e: Exception) {
            // Fall back to Room local DB if Firestore fails
            userDao.getUserById(userId) ?: throw Exception("Failed to fetch user: ${e.message}")
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getByEmail(email)
    }
    suspend fun updateUser(user: User) {
        userDao.update(user)
        db.collection("users").document(user.uid).set(user).await()
    }

    suspend fun saveUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}
