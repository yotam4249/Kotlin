package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.ui.viewmodel.PostViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class EditPostFragment : Fragment() {

    private val args: EditPostFragmentArgs by navArgs()
    private lateinit var viewModel: PostViewModel

    private lateinit var nameEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imageView: ImageView
    private lateinit var changeImageBtn: Button
    private lateinit var saveButton: Button
    private lateinit var inputBrand: MaterialAutoCompleteTextView
    private lateinit var inputCategory: MaterialAutoCompleteTextView

    private var newImageUri: Uri? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            newImageUri = it
            imageView.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_edit_post, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        nameEditText = view.findViewById(R.id.edit_post_name)
        ratingEditText = view.findViewById(R.id.edit_post_rating)
        descriptionEditText = view.findViewById(R.id.edit_post_description)
        imageView = view.findViewById(R.id.edit_post_image)
        changeImageBtn = view.findViewById(R.id.btn_change_image)
        saveButton = view.findViewById(R.id.btn_save_post)
        inputBrand = view.findViewById(R.id.edit_post_brand)
        inputCategory = view.findViewById(R.id.edit_post_category)

        val post = args.post

        nameEditText.setText(post.name)
        ratingEditText.setText(post.rating.toString())
        descriptionEditText.setText(post.shoeDescription)
        inputBrand.setText(post.brand, false)
        inputCategory.setText(post.category ?: "", false)
        Glide.with(this).load(post.shoeUrl).into(imageView)

        val brandOptions = listOf("Nike", "Adidas", "New Balance", "Puma", "Asics")
        val categoryOptions = listOf("Casual", "Running", "Dress", "Hiking", "Basketball")

        inputBrand.apply {
            setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, brandOptions))
            setOnClickListener { showDropDown() }
            keyListener = null
            isCursorVisible = false
            isFocusable = false
            isFocusableInTouchMode = false
        }

        inputCategory.apply {
            setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryOptions))
            setOnClickListener { showDropDown() }
            keyListener = null
            isCursorVisible = false
            isFocusable = false
            isFocusableInTouchMode = false
        }

        changeImageBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        saveButton.setOnClickListener {
            val imagePath = newImageUri?.let { uri ->
                ImageHandler.saveImageToStorage(requireContext(), uri)
            } ?: post.shoeUrl

            val updatedPost = post.copy(
                name = nameEditText.text.toString(),
                brand = inputBrand.text.toString(),
                rating = ratingEditText.text.toString().toFloatOrNull() ?: 0f,
                shoeDescription = descriptionEditText.text.toString(),
                category = inputCategory.text.toString(),
                shoeUrl = imagePath
            )

            viewModel.updatePost(updatedPost)
            Toast.makeText(requireContext(), "Post updated", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}
