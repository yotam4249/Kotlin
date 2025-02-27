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

class PostAdapter(private var posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.post_name)
        private val brandTextView: TextView = itemView.findViewById(R.id.post_brand)
        private val ratingTextView: TextView = itemView.findViewById(R.id.post_rating)
        private val imageView: ImageView = itemView.findViewById(R.id.post_image)

        fun bind(post: Post) {
            nameTextView.text = post.name
            brandTextView.text = post.brand
            ratingTextView.text = "⭐ ${post.rating}"
            Glide.with(itemView.context).load(post.shoeUrl).into(imageView)
        }
    }
}
