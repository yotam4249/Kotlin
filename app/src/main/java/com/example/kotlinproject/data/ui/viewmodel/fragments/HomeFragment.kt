package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.databinding.FragmentHomeBinding
import com.example.kotlinproject.data.ui.viewmodel.PostViewModel
import com.example.kotlinproject.data.ui.viewmodel.fragments.PostAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val args: HomeFragmentArgs by navArgs() // SafeArgs
    private val postViewModel: PostViewModel by viewModels()
    private lateinit var adapter: PostAdapter
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = args.user
        view.findViewById<Button>(R.id.shoe_api_btn).setOnClickListener {
            findNavController().navigate(R.id.action_global_shoeApiFragment)
        }

        setupRecyclerView()
        observePosts()
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter(emptyList())
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter
    }

    private fun observePosts() {
        postViewModel.allPosts.observe(viewLifecycleOwner) { posts ->
            adapter.updatePosts(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
