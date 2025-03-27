package com.example.kotlinproject.data.ui.viewmodel.fragments

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.navigation.findNavController
//import com.example.kotlinproject.R
//import com.example.kotlinproject.databinding.FragmentProfileBinding
//
//class ProfileFragment : Fragment() {
//
//    private var _binding : FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentProfileBinding.inflate(inflater,container,false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.ivSettings.setOnClickListener{
//            requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
//        }
//    }

//}

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostRepository
import com.example.kotlinproject.data.ui.viewmodel.fragments.UserRepository
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostAdapter
import com.example.kotlinproject.data.ui.viewmodel.fragments.EditProfileFragment
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.navArgs
import com.example.kotlinproject.AuthActivity
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.model.User

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(PostRepository(requireContext()), UserRepository(requireContext()))
    }
    private val args : ProfileFragmentArgs by navArgs()
    private lateinit var user : User
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

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.fetchProfile()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = UserRepository(requireContext())
        val passedUser = args.user

        // Load latest user from Room
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
        }
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            userNameTextView.text = user.name
            emailTextView.text = user.email
            Glide.with(this).load(user.photoUrl).into(avatarImageView)
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
            postAdapter.updatePosts(posts)
        })
    }

    private fun setupListeners() {
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            // Start a new activity that holds your LoginFragment
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
