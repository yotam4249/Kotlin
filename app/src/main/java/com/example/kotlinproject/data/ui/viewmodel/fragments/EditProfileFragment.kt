package com.example.kotlinproject.data.ui.viewmodel.fragments
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.database.PostDatabase
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.data.ui.viewmodel.fragments.EditProfileFragmentArgs
import com.google.firebase.auth.FirebaseAuth


////
////import android.app.Activity
////import android.content.Intent
////import android.net.Uri
////import android.os.Bundle
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import android.widget.*
////import androidx.activity.result.ActivityResultLauncher
////import androidx.activity.result.contract.ActivityResultContracts
////import androidx.fragment.app.Fragment
////import androidx.fragment.app.viewModels
////import androidx.lifecycle.Observer
////import androidx.navigation.fragment.findNavController
////import com.bumptech.glide.Glide
////import com.example.kotlinproject.R
////import com.yalantis.ucrop.UCrop
////import java.io.File
////import androidx.navigation.fragment.navArgs
////import com.example.kotlinproject.data.model.User
////
////class EditProfileFragment : Fragment() {
////
////    private val viewModel: EditProfileViewModel by viewModels {
////        EditProfileViewModelFactory(UserRepository(requireContext()))
////    }
////
////    private lateinit var avatarImageView: ImageView
////    private lateinit var editGalleryButton: ImageButton
////    private lateinit var editCameraButton: ImageButton
////    private lateinit var editConfirmButton: Button
////    private lateinit var usernameEditText: EditText
////    private lateinit var emailTextView: TextView
////    private lateinit var progressBar: ProgressBar
////    private lateinit var user : User
////    private val imagePicker: ActivityResultLauncher<String> = getImagePicker()
////    private val cameraPicker: ActivityResultLauncher<Intent> = getCameraPicker()
////    private val uCropLauncher: ActivityResultLauncher<Intent> = getUCropLauncher()
////    private val args: EditProfileFragmentArgs by navArgs()
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        user = args.user
////    }
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View {
////        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
////
////        avatarImageView = view.findViewById(R.id.edit_profile_avatar)
////        editGalleryButton = view.findViewById(R.id.edit_profile_gallery_btn)
////        editCameraButton = view.findViewById(R.id.edit_profile_camera_btn)
////        editConfirmButton = view.findViewById(R.id.edit_profile_confirm_btn)
////        usernameEditText = view.findViewById(R.id.username_edit_text)
////        emailTextView = view.findViewById(R.id.email_my_profile)
////        progressBar = view.findViewById(R.id.progress_bar)
////
////        setupObservers()
////        setupListeners()
////
////        return view
////    }
////
////    private fun setupObservers() {
////        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
////            // Show user data in the UI
////            usernameEditText.setText(user.name) // changed from user.uid
////            emailTextView.text = user.email
////            Glide.with(this).load(user.photoUrl).into(avatarImageView)
////        })
////
////        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
////            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
////            editConfirmButton.isEnabled = !isLoading
////        })
////    }
////
////    private fun setupListeners() {
////        editGalleryButton.setOnClickListener { imagePicker.launch("image/*") }
////        editCameraButton.setOnClickListener { openCamera() }
////
////        editConfirmButton.setOnClickListener {
////            viewModel.username.value = usernameEditText.text.toString()
////            viewModel.updateUserProfile(
////                onSuccess = {
////                    Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
////                    findNavController().navigate(R.id.EditProfile)
////                },
////                onFailure = { e ->
////                    Toast.makeText(requireContext(), "Error: ${e?.message}", Toast.LENGTH_SHORT).show()
////                }
////            )
////        }
////    }
////
////    private fun getImagePicker() =
////        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
////            uri?.let { startCrop(it) }
////        }
////
////    private fun getCameraPicker() =
////        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
////            if (result.resultCode == Activity.RESULT_OK) {
////                val uri = result.data?.data
////                uri?.let { startCrop(it) }
////            }
////        }
////
////    private fun getUCropLauncher() =
////        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
////            if (result.resultCode == Activity.RESULT_OK) {
////                val uri = UCrop.getOutput(result.data!!)
////                uri?.let { viewModel.updateAvatar(it) }
////            }
////        }
////
////    private fun openCamera() {
////        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
////        cameraPicker.launch(cameraIntent)
////    }
////
////    private fun startCrop(sourceUri: Uri) {
////        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped.jpg"))
////        val cropIntent = UCrop.of(sourceUri, destinationUri)
////            .withAspectRatio(1f, 1f)
////            .withMaxResultSize(200, 200)
////            .getIntent(requireContext())
////
////        uCropLauncher.launch(cropIntent)
////    }
////}
//
//
//package com.example.kotlinproject.data.ui.viewmodel.fragments
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.text.InputType
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import com.bumptech.glide.Glide
//import com.example.kotlinproject.R
//import com.example.kotlinproject.data.model.User
//import com.yalantis.ucrop.UCrop
//import com.google.firebase.auth.FirebaseAuth
//import java.io.File
//
//class EditProfileFragment : Fragment() {
//
//    private val viewModel: EditProfileViewModel by viewModels {
//        EditProfileViewModelFactory(UserRepository(requireContext()))
//    }
//
//    private lateinit var avatarImageView: ImageView
//    private lateinit var editGalleryButton: ImageButton
//    private lateinit var editCameraButton: ImageButton
//    private lateinit var editConfirmButton: Button
//    private lateinit var editNameButton: ImageButton
//    private lateinit var usernameEditText: EditText
//    private lateinit var emailTextView: TextView
//    private lateinit var changePasswordButton: Button
//    private lateinit var progressBar: ProgressBar
//    private lateinit var user: User
//
//    private val imagePicker: ActivityResultLauncher<String> = getImagePicker()
//    private val cameraPicker: ActivityResultLauncher<Intent> = getCameraPicker()
//    private val uCropLauncher: ActivityResultLauncher<Intent> = getUCropLauncher()
//    private val args: EditProfileFragmentArgs by navArgs()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        user = args.user
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
//
//        avatarImageView = view.findViewById(R.id.edit_profile_avatar)
//        editGalleryButton = view.findViewById(R.id.edit_profile_gallery_btn)
//        editCameraButton = view.findViewById(R.id.edit_profile_camera_btn)
//        editConfirmButton = view.findViewById(R.id.edit_profile_confirm_btn)
//        editNameButton = view.findViewById(R.id.edit_name_btn)
//        usernameEditText = view.findViewById(R.id.username_edit_text)
//        emailTextView = view.findViewById(R.id.email_my_profile)
//        progressBar = view.findViewById(R.id.progress_bar)
//        changePasswordButton = view.findViewById(R.id.change_password_btn)
//
//        // Display current user info
//        usernameEditText.setText(user.name)
//        emailTextView.text = user.email
//        Glide.with(this).load(user.photoUrl).into(avatarImageView)
//        usernameEditText.isEnabled = false
//
//        setupListeners()
//
//        return view
//    }
//
//    private fun setupListeners() {
//        editGalleryButton.setOnClickListener { imagePicker.launch("image/*") }
//        editCameraButton.setOnClickListener { openCamera() }
//
//        // Enable name editing
//        editNameButton.setOnClickListener {
//            usernameEditText.isEnabled = true
//            usernameEditText.requestFocus()
//            Toast.makeText(requireContext(), "Name editing enabled", Toast.LENGTH_SHORT).show()
//        }
//
//        // Confirm save
//        editConfirmButton.setOnClickListener {
//            viewModel.username.value = usernameEditText.text.toString()
//            viewModel.updateUserProfile(
//                onSuccess = {
//                    Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
//                    findNavController().popBackStack()
//                },
//                onFailure = { e ->
//                    Toast.makeText(requireContext(), "Error: ${e?.message}", Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
//
//        // Change password
//        changePasswordButton.setOnClickListener {
//            showChangePasswordDialog()
//        }
//    }
//
//    private fun showChangePasswordDialog() {
//        val editText = EditText(requireContext())
//        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
//        editText.hint = "Enter new password"
//
//        AlertDialog.Builder(requireContext())
//            .setTitle("Change Password")
//            .setView(editText)
//            .setPositiveButton("Change") { _, _ ->
//                val newPassword = editText.text.toString()
//                if (newPassword.length < 6) {
//                    Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
//                    return@setPositiveButton
//                }
//
//                val user = FirebaseAuth.getInstance().currentUser
//                user?.updatePassword(newPassword)
//                    ?.addOnSuccessListener {
//                        Toast.makeText(requireContext(), "Password updated", Toast.LENGTH_SHORT).show()
//                    }
//                    ?.addOnFailureListener {
//                        Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show()
//                    }
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    private fun getImagePicker() =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let { startCrop(it) }
//        }
//
//    private fun getCameraPicker() =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri = result.data?.data
//                uri?.let { startCrop(it) }
//            }
//        }
//
//    private fun getUCropLauncher() =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri = UCrop.getOutput(result.data!!)
//                uri?.let { viewModel.updateAvatar(it) }
//            }
//        }
//
//    private fun openCamera() {
//        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraPicker.launch(cameraIntent)
//    }
//
//    private fun startCrop(sourceUri: Uri) {
//        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped.jpg"))
//        val cropIntent = UCrop.of(sourceUri, destinationUri)
//            .withAspectRatio(1f, 1f)
//            .withMaxResultSize(200, 200)
//            .getIntent(requireContext())
//
//        uCropLauncher.launch(cropIntent)
//    }
//}
class EditProfileFragment : Fragment(){
    private val args : EditProfileFragmentArgs by navArgs()

    private lateinit var viewModel: EditProfileViewModel
    private lateinit var imageView:ImageView
    private lateinit var usernameEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var confirmBtn: Button
    private lateinit var galleryBtn: ImageButton
    private lateinit var user : User
    private lateinit var emailTextView:TextView
    private lateinit var editNameBtn: ImageButton
    private lateinit var changePasswordBtn: Button
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        uri?.let {
            viewModel.newProfileImageUri = uri  // Clearly store URI here
            imageView.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.user
        val repo = UserRepository(requireContext())
        val imageHandler = ImageHandler
        val postDao = PostDatabase.getDatabase(requireContext()).postDao() // clearly inject PostDao
        val factory = EditProfileViewModelFactory(repo, imageHandler, postDao)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        viewModel.loadUser(user)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile,container,false)
        imageView = view.findViewById(R.id.edit_profile_avatar)
        usernameEditText = view.findViewById(R.id.username_edit_text)
        progressBar = view.findViewById(R.id.progress_bar)
        confirmBtn = view.findViewById(R.id.edit_profile_confirm_btn)
        galleryBtn = view.findViewById(R.id.edit_profile_gallery_btn)
        editNameBtn = view.findViewById(R.id.edit_name_btn)
        changePasswordBtn = view.findViewById(R.id.change_password_btn)
        emailTextView = view.findViewById(R.id.email_my_profile)

        emailTextView.text = user.email
        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers(){
        viewModel.user.observe(viewLifecycleOwner, Observer { updatedUser ->
            if (updatedUser != null) {
                usernameEditText.setText(updatedUser.name)
            }
            val imagePath = updatedUser?.photoUrl
            if(!imagePath.isNullOrEmpty()){
                val file = ImageHandler.getImageFile(requireContext(),imagePath)
                Glide.with(this)
                    .load(file)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.avatar_icon)
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        galleryBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        editNameBtn.setOnClickListener {
            usernameEditText.isEnabled = true
            usernameEditText.requestFocus()
        }

        confirmBtn.setOnClickListener {
            val userId = user.uid // Clearly use uid
            val newUserName = usernameEditText.text.toString()

            viewModel.username.value = newUserName

            viewModel.updateUserProfile(
                userId = userId,
                imageUri = viewModel.newProfileImageUri,
                context = requireContext(),
                onSuccess = {
                    Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                    val updatedUser = viewModel.user.value!!
                    val action = EditProfileFragmentDirections.actionGlobalProfileFragment(updatedUser)
                    findNavController().navigate(action)
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Error: ${it?.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }



        changePasswordBtn.setOnClickListener {
            showChangePasswordDialog()
        }
    }

    private fun showChangePasswordDialog() {
        val input = EditText(requireContext()).apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            hint = "Enter new password"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Change Password")
            .setView(input)
            .setPositiveButton("Change") { _, _ ->
                val newPassword = input.text.toString()
                if (newPassword.length < 6) {
                    Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)
                    ?.addOnSuccessListener {
                        Toast.makeText(requireContext(), "Password updated", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}