package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.R
import java.io.File

class PostAdapter(private var posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
        holder.bind(posts[position])

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.post_name)
        private val brandTextView: TextView = itemView.findViewById(R.id.post_brand)
        private val ratingTextView: TextView = itemView.findViewById(R.id.post_rating)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.post_description)
        private val categoryTextView: TextView = itemView.findViewById(R.id.post_category)
        private val usernameTextView: TextView = itemView.findViewById(R.id.post_username)
        private val likeCountTextView: TextView = itemView.findViewById(R.id.post_like_count)
        private val postImageView: ImageView = itemView.findViewById(R.id.post_image)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.post_avatar)

        fun bind(post: Post) {
            nameTextView.text = post.name
            brandTextView.text = post.brand
            ratingTextView.text = "⭐ ${post.rating}"
            descriptionTextView.text = post.shoeDescription
            categoryTextView.text = post.category
            usernameTextView.text = post.userName
            likeCountTextView.text = post.like.toString()

            // Load post image
            val postImageFile = File(post.shoeUrl ?: "")
            Glide.with(itemView.context)
                .load(if (postImageFile.exists()) postImageFile else R.drawable.shoe_avatar)
                .into(postImageView)

            // Load avatar
            val avatarFile = File(post.avatarUrl)
            Glide.with(itemView.context)
                .load(if (avatarFile.exists()) avatarFile else R.drawable.avatar_icon)
                .circleCrop()
                .into(avatarImageView)
        }
    }
}
