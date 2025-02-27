package com.example.kotlinproject.data.ui.viewmodel.fragments



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ui.viewmodel.fragments.UserRepository
import com.yalantis.ucrop.UCrop
import java.io.File

class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory(UserRepository(requireContext()))
    }

    private lateinit var avatarImageView: ImageView
    private lateinit var editGalleryButton: ImageButton
    private lateinit var editCameraButton: ImageButton
    private lateinit var editConfirmButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var emailTextView: TextView
    private lateinit var progressBar: ProgressBar

    private val imagePicker: ActivityResultLauncher<String> = getImagePicker()
    private val cameraPicker: ActivityResultLauncher<Intent> = getCameraPicker()
    private val uCropLauncher: ActivityResultLauncher<Intent> = getUCropLauncher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        avatarImageView = view.findViewById(R.id.edit_profile_avatar)
        editGalleryButton = view.findViewById(R.id.edit_profile_gallery_btn)
        editCameraButton = view.findViewById(R.id.edit_profile_camera_btn)
        editConfirmButton = view.findViewById(R.id.edit_profile_confirm_btn)
        usernameEditText = view.findViewById(R.id.username_edit_text)
        emailTextView = view.findViewById(R.id.email_my_profile)
        progressBar = view.findViewById(R.id.progress_bar)

        setupObservers()
        setupListeners()

        viewModel.fetchUserProfile()

        return view
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            usernameEditText.setText(user.uid)
            emailTextView.text = user.email
            Glide.with(this).load(user.photoUrl).into(avatarImageView)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            editConfirmButton.isEnabled = !isLoading
        })
    }

    private fun setupListeners() {
        editGalleryButton.setOnClickListener { imagePicker.launch("image/*") }
        editCameraButton.setOnClickListener { openCamera() }

        editConfirmButton.setOnClickListener {
            viewModel.username.value = usernameEditText.text.toString()
            viewModel.updateUserProfile(
                onSuccess = {
                    Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.EditProfile)
                },
                onFailure = { e ->
                    Toast.makeText(requireContext(), "Error: ${e?.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun getImagePicker() =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { startCrop(it) }
        }

    private fun getCameraPicker() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let { startCrop(it) }
            }
        }

    private fun getUCropLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = UCrop.getOutput(result.data!!)
                uri?.let { viewModel.updateAvatar(it) }
            }
        }

    private fun openCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        cameraPicker.launch(cameraIntent)
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped.jpg"))
        val cropIntent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(200, 200)
            .getIntent(requireContext())

        uCropLauncher.launch(cropIntent)
    }
}
