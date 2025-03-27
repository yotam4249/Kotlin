package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.model.Post
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.data.ui.viewmodel.PostViewModel
import com.example.kotlinproject.databinding.FragmentAddPostBinding
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class AddPostFragment : Fragment() {

    private val args: AddPostFragmentArgs by navArgs()
    private lateinit var currentUser: User
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by viewModels()
    private var croppedImageUri: Uri? = null

    private val cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            UCrop.getOutput(result.data!!)?.let { uri ->
                croppedImageUri = uri
                Glide.with(this).load(uri).into(binding.postImageView)
            }
        } else {
            Toast.makeText(requireContext(), "Crop canceled.", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { launchImageCrop(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = args.user
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnChooseImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSavePost.setOnClickListener {
            savePostToDB()
        }
    }

    private fun launchImageCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "post_${System.currentTimeMillis()}.jpg"))
        val options = UCrop.Options().apply { setCompressionQuality(90) }

        cropImageLauncher.launch(UCrop.of(uri, destinationUri).withOptions(options).getIntent(requireContext()))
    }

    private fun savePostToDB() {
        val name = binding.etPostName.text.toString()
        val brand = binding.etPostBrand.text.toString()
        val rating = binding.etPostRating.text.toString().toFloatOrNull() ?: 0f
        val description = binding.etPostDescription.text.toString()
        val price = binding.etPostPrice.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.etPostCategory.text.toString()

        if (croppedImageUri == null || name.isBlank() || brand.isBlank() || description.isBlank() || category.isBlank()) {
            Toast.makeText(requireContext(), "All fields and image required", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val imagePath = ImageHandler.saveImageToStorage(requireContext(), croppedImageUri!!)
            val newPost = Post(
                name = name,
                brand = brand,
                rating = rating,
                userName = currentUser.name,
                avatarUrl = currentUser.photoUrl ?: "",
                shoeUrl = imagePath,
                shoeDescription = description,
                shoePrice = price,
                like = 0,
                category = category,
                lastUpdated = System.currentTimeMillis()
            )
            postViewModel.addPost(newPost)

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Post added successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
