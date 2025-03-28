package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.ui.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

    private val args: EditPostFragmentArgs by navArgs()
    private lateinit var viewModel: PostViewModel

    private lateinit var nameEditText: EditText
    private lateinit var brandEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        nameEditText = view.findViewById(R.id.edit_post_name)
        brandEditText = view.findViewById(R.id.edit_post_brand)
        ratingEditText = view.findViewById(R.id.edit_post_rating)
        descriptionEditText = view.findViewById(R.id.edit_post_description)
        categoryEditText = view.findViewById(R.id.edit_post_category)
        saveButton = view.findViewById(R.id.btn_save_post)

        val post = args.post

        nameEditText.setText(post.name)
        brandEditText.setText(post.brand)
        ratingEditText.setText(post.rating.toString())
        descriptionEditText.setText(post.shoeDescription)
        categoryEditText.setText(post.category)

        saveButton.setOnClickListener {
            val updatedPost = post.copy(
                name = nameEditText.text.toString(),
                brand = brandEditText.text.toString(),
                rating = ratingEditText.text.toString().toFloatOrNull() ?: 0f,
                shoeDescription = descriptionEditText.text.toString(),
                category = categoryEditText.text.toString()
            )
            viewModel.updatePost(updatedPost)
            Toast.makeText(requireContext(), "Post updated", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}