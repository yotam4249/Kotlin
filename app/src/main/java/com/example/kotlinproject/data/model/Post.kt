package com.example.kotlinproject.data.model

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinproject.data.ui.viewmodel.fragments.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.gson.annotations.SerializedName


@Entity(tableName = "posts")

data class Post(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = "",

    @ColumnInfo(name = "brand")
    @SerializedName("brand")
    val brand: String = "",

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    val rating: Float = 0.0f,

    @ColumnInfo(name = "user_name")
    @SerializedName("userName")
    var userName: String = "",

    @ColumnInfo(name = "avatar_url")
    @SerializedName("avatarUrl")
    var avatarUrl: String = "",

    @ColumnInfo(name = "shoe_description")
    @SerializedName("shoeDescription")
    var shoeDescription: String? = "",

    @ColumnInfo(name = "shoe_price")
    @SerializedName("shoePrice")
    var shoePrice: Double = 0.0,

    @ColumnInfo(name = "shoe_url")
    @SerializedName("shoeUrl")
    var shoeUrl: String? = "",

    @ColumnInfo(name = "like_url")
    @SerializedName("likeUrl")
    var like: Int? = 0,

    @ColumnInfo(name = "category")
    @SerializedName("category")
    var category: String? = "",

    @ColumnInfo(name = "last_updated")
    var lastUpdated: Long? = null
    ){

    companion object{
        private const val ID = "id"
        private const val NAME = "name"
        private const val BRAND = "brand"
        private const val RATING = "rating"
        private const val USER_NAME = "username"
        private const val AVATAR = "avatar"
        private const val DESCRIPTION = "description"
        private const val PRICE = "price"
        private const val IMAGE_URL = "imageUrl"
        private const val LIKE = "like"
        private const val CATEGORY = "category"
        private const val LAST_UPDATED = "lastUpdated"
        private const val LOCAL_LAST_UPDATED = "posts_local_last_updated"
        fun fromJson(json: Map<String, Any>): Post {
            return Post(
                id = (json[ID] as? Long)?.toInt() ?: 0,
                name = json[NAME] as? String ?: "",
                brand = json[BRAND] as? String ?: "",
                rating = (json[RATING] as? Double)?.toFloat() ?: 0f,
                userName = json[USER_NAME] as? String ?: "",
                avatarUrl = json[AVATAR] as? String ?: "",
                shoeDescription = json[DESCRIPTION] as? String,
                shoePrice = json[PRICE] as? Double ?: 0.0,
                shoeUrl = json[IMAGE_URL] as? String,
                like = json[LIKE] as? Int?: 0,
                category = json[CATEGORY] as? String
                ).apply { lastUpdated = (json[LAST_UPDATED] as? Timestamp)?.seconds }
                }
            }
    fun getPostLocalLastUpdate(): Long {
        val sharedPref = MyApplication.getMyContext()
            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0)
    }

    fun setPostLocalLastUpdate(time: Long) {
        MyApplication.getMyContext()
            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
            .edit()
            .putLong(LOCAL_LAST_UPDATED, time)
            .apply()
    }

    val json: HashMap<String, Any?>
        get() = hashMapOf(
            ID to id,
            NAME to name,
            BRAND to brand,
            RATING to rating,
            USER_NAME to userName,
            AVATAR to avatarUrl,
            DESCRIPTION to shoeDescription,
            PRICE to shoePrice,
            IMAGE_URL to shoeUrl,
            LIKE to like,
            CATEGORY to category,
            LAST_UPDATED to FieldValue.serverTimestamp()
        )



        }






