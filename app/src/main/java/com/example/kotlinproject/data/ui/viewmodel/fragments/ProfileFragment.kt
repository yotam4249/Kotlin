package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.AuthActivity
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(PostRepository(requireContext()), UserRepository(requireContext()))
    }

    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var user: User
    private lateinit var avatarImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logOutButton: TextView
    private lateinit var editButton: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        avatarImageView = view.findViewById(R.id.avatar_my_profile)
        userNameTextView = view.findViewById(R.id.userName_my_profile)
        emailTextView = view.findViewById(R.id.email_my_profile)
        logOutButton = view.findViewById(R.id.log_out_btn_my_profile)
        editButton = view.findViewById(R.id.edit_btn_my_profile)
        recyclerView = view.findViewById(R.id.recyclerView)

        setupListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = UserRepository(requireContext())
        val passedUser = args.user

        lifecycleScope.launchWhenStarted {
            val freshUser = userRepository.getUserByEmail(passedUser.email) ?: passedUser
            user = freshUser

            userNameTextView.text = user.name
            emailTextView.text = user.email

            if (!user.photoUrl.isNullOrEmpty()) {
                val file = ImageHandler.getImageFile(requireContext(), user.photoUrl!!)
                if (file != null) {
                    Glide.with(this@ProfileFragment).load(file).into(avatarImageView)
                } else {
                    avatarImageView.setImageResource(R.drawable.avatar_icon)
                }
            } else {
                avatarImageView.setImageResource(R.drawable.avatar_icon)
            }

            setupRecyclerView()
            observeUserPosts(user.name)
        }
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(emptyList(), user.uid,
            onEditClick = { post ->
                val action = ProfileFragmentDirections.actionProfileToEditPost(post)
                findNavController().navigate(action)
            },
            onDeleteClick = { post ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Post")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Delete") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deletePost(post)
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter
    }

    private fun observeUserPosts(username: String) {
        viewModel.getPostsByUser(username).observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }
    }

    private fun setupListeners() {
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        editButton.setOnClickListener {
            val action = ProfileFragmentDirections.actionGlobalEditProfile(user)
            findNavController().navigate(action)
        }
    }
}
