//package com.example.kotlinproject.data.ui.viewmodel.fragments
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelStoreOwner
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.kotlinproject.R
//import com.example.kotlinproject.data.model.Post
//import com.example.kotlinproject.data.ui.viewmodel.PostViewModel
//import java.io.File
//
//class PostAdapter(
//    private var posts: List<Post>,
//    private val userId: String
//) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
//
//    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val nameTextView: TextView = itemView.findViewById(R.id.post_name)
//        private val brandTextView: TextView = itemView.findViewById(R.id.post_brand)
//        private val ratingTextView: TextView = itemView.findViewById(R.id.post_rating)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.post_description)
//        private val categoryTextView: TextView = itemView.findViewById(R.id.post_category)
//        private val usernameTextView: TextView = itemView.findViewById(R.id.post_username)
//        private val likeCountTextView: TextView = itemView.findViewById(R.id.post_like_count)
//        private val postImageView: ImageView = itemView.findViewById(R.id.post_image)
//        private val avatarImageView: ImageView = itemView.findViewById(R.id.post_avatar)
//        private val likeIcon: ImageView = itemView.findViewById(R.id.post_like_icon)
//
//        fun bind(post: Post, userId: String) {
//            nameTextView.text = post.name
//            brandTextView.text = post.brand
//            ratingTextView.text = "⭐ ${post.rating}"
//            descriptionTextView.text = post.shoeDescription
//            categoryTextView.text = post.category
//            usernameTextView.text = post.userName
//            likeCountTextView.text = post.like.toString()
//
//            // Load post image
//            val postImageFile = File(post.shoeUrl ?: "")
//            Glide.with(itemView.context)
//                .load(if (postImageFile.exists()) postImageFile else R.drawable.shoe_avatar)
//                .into(postImageView)
//
//            // Load avatar
//            val avatarFile = File(post.avatarUrl)
//            Glide.with(itemView.context)
//                .load(if (avatarFile.exists()) avatarFile else R.drawable.avatar_icon)
//                .circleCrop()
//                .into(avatarImageView)
//
//            // Handle like icon click
//            likeIcon.setOnClickListener {
//                val context = itemView.context
//                val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[PostViewModel::class.java]
//                viewModel.likePost(post.id, userId) { newCount ->
//                    likeCountTextView.text = newCount.toString()
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
//        return PostViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//        holder.bind(posts[position], userId)
//    }
//
//    override fun getItemCount(): Int = posts.size
//
//    fun updatePosts(newPosts: List<Post>) {
//        posts = newPosts
//        notifyDataSetChanged()
//    }
//
//    fun updateUserInAllPosts(userId: String, newUserName: String, newAvatarUrl: String) {
//        val updatedPosts = posts.map { post ->
//            if (post.userId == userId) {
//                post.copy(userName = newUserName, avatarUrl = newAvatarUrl)
//            } else post
//        }
//        updatePosts(updatedPosts)
//    }
//}

package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.ui.viewmodel.PostViewModel
import java.io.File

class PostAdapter(
    private var posts: List<Post>,
    private val userId: String,
    private val onEditClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val priceTextView: TextView = itemView.findViewById(R.id.post_price)
        private val nameTextView: TextView = itemView.findViewById(R.id.post_name)
        private val brandTextView: TextView = itemView.findViewById(R.id.post_brand)
        private val ratingTextView: TextView = itemView.findViewById(R.id.post_rating)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.post_description)
        private val categoryTextView: TextView = itemView.findViewById(R.id.post_category)
        private val usernameTextView: TextView = itemView.findViewById(R.id.post_username)
        private val likeCountTextView: TextView = itemView.findViewById(R.id.post_like_count)
        private val postImageView: ImageView = itemView.findViewById(R.id.post_image)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.post_avatar)
        private val likeIcon: ImageView = itemView.findViewById(R.id.post_like_icon)
        private val editIcon: ImageView = itemView.findViewById(R.id.post_edit_icon)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.post_delete_icon)
        private val ownerActions: View = itemView.findViewById(R.id.post_owner_actions)

        fun bind(post: Post, userId: String) {
            priceTextView.text = "$${post.shoePrice}"
            nameTextView.text = post.name
            brandTextView.text = post.brand
            ratingTextView.text = "\u2B50 ${post.rating}"
            descriptionTextView.text = post.shoeDescription
            categoryTextView.text = post.category
            usernameTextView.text = post.userName
            likeCountTextView.text = post.like.toString()

            val postImageFile = File(post.shoeUrl ?: "")
            Glide.with(itemView.context)
                .load(if (postImageFile.exists()) postImageFile else R.drawable.shoe_avatar)
                .into(postImageView)

            val avatarFile = File(post.avatarUrl)
            Glide.with(itemView.context)
                .load(if (avatarFile.exists()) avatarFile else R.drawable.avatar_icon)
                .circleCrop()
                .into(avatarImageView)

            likeIcon.setOnClickListener {
                val context = itemView.context
                val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[PostViewModel::class.java]
                viewModel.likePost(post.id, userId) { newCount ->
                    likeCountTextView.text = newCount.toString()
                }
            }

            if (post.userId == userId) {
                ownerActions.visibility = View.VISIBLE
                editIcon.setOnClickListener { onEditClick(post) }
                deleteIcon.setOnClickListener { onDeleteClick(post) }
            } else {
                ownerActions.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], userId)
    }

    fun updateUserInAllPosts(userId: String, newUserName: String, newAvatarUrl: String) {
                val updatedPosts = posts.map { post ->
            if (post.userId == userId) {
                post.copy(userName = newUserName, avatarUrl = newAvatarUrl)
            } else post
        }
        updatePosts(updatedPosts)
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
