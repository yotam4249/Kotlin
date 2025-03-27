package com.example.kotlinproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kotlinproject.data.model.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserById(uid: String): User?

    @Query("SELECT * FROM users WHERE email= :email")
    suspend fun getByEmail(email:String): User?

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Update
    suspend fun update(user: User)
}