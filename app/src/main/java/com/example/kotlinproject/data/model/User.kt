package com.example.kotlinproject.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid:String,
    val name:String,
    val email:String,
    val photoUrl:String? = null,

) : Parcelable