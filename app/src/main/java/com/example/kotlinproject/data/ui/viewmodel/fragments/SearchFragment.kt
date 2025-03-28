package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.User
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SearchFragment : Fragment() {

    private val args: SearchFragmentArgs by navArgs()
    private lateinit var currentUser: User
    private lateinit var postRepository: PostRepository
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = args.user
        postRepository = PostRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val inputCategory = view.findViewById<MaterialAutoCompleteTextView>(R.id.inputCategory)
        val inputBrand = view.findViewById<MaterialAutoCompleteTextView>(R.id.inputBrand)
        val inputMaxPrice = view.findViewById<EditText>(R.id.inputMaxPrice)
        val inputMinRating = view.findViewById<EditText>(R.id.inputMinRating)
        val searchButton = view.findViewById<Button>(R.id.btnSearch)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSearchResults)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter(
            emptyList(),
           currentUser.uid,
            onEditClick = {},
            onDeleteClick = {}
        )
        recyclerView.adapter = postAdapter

        val brandOptions = listOf("Nike", "Adidas", "New Balance", "Puma", "Asics")
        val categoryOptions = listOf("Casual", "Running", "Dress", "Hiking", "Basketball")

        inputBrand.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, brandOptions))
        inputCategory.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryOptions))

        inputBrand.setOnClickListener {
            inputBrand.showDropDown()
        }

        inputCategory.setOnClickListener {
            inputCategory.showDropDown()
        }
        searchButton.setOnClickListener {
            when {
                inputCategory.text.isNotBlank() -> {
                    postRepository.getPostsByCategory(inputCategory.text.toString())
                        .observe(viewLifecycleOwner) { postAdapter.updatePosts(it) }
                }
                inputBrand.text.isNotBlank() -> {
                    postRepository.getPostsByBrand(inputBrand.text.toString())
                        .observe(viewLifecycleOwner) { postAdapter.updatePosts(it) }
                }
                inputMaxPrice.text.isNotBlank() -> {
                    inputMaxPrice.text.toString().toDoubleOrNull()?.let {
                        postRepository.getPostsByPrice(it)
                            .observe(viewLifecycleOwner) { postAdapter.updatePosts(it) }
                    }
                }
                inputMinRating.text.isNotBlank() -> {
                    inputMinRating.text.toString().toFloatOrNull()?.let {
                        postRepository.getPostsByRating(it)
                            .observe(viewLifecycleOwner) { postAdapter.updatePosts(it) }
                    }
                }
            }
        }
    }
}
