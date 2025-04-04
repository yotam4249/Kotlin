package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kotlinproject.MainActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.data.ImageHandler
import com.example.kotlinproject.data.dao.UserDao
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var userDao: UserDao
    private var croppedImageUri: Uri? = null

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                UCrop.getOutput(result.data!!)?.let { uri ->
                    croppedImageUri = uri
                    Glide.with(this).load(uri).into(binding.ivAvatar)
                }
            } else {
                Toast.makeText(requireContext(), "Crop canceled or failed.", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { launchImageCrop(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            registerUser(email, password, name)
        }

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun launchImageCrop(uri: Uri) {
        val destinationUri =
            Uri.fromFile(File(requireContext().cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))

        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            setFreeStyleCropEnabled(false)
            withAspectRatio(1f, 1f)

            // Customizations (optional, fully supported)
            setToolbarColor(ContextCompat.getColor(requireContext(), R.color.black))
            setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.black))
            setToolbarWidgetColor(ContextCompat.getColor(requireContext(), R.color.white))
            setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(), R.color.white))
            setToolbarTitle("Crop Profile Image")
        }

        cropImageLauncher.launch(
            UCrop.of(uri, destinationUri).withOptions(options).getIntent(requireContext())
        )
    }

    private fun registerUser(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let { firebaseUser ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            val existingUser = userDao.getUserById(firebaseUser.uid)
                            if (existingUser != null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(requireContext(), "User already exists! Please log in.", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }

                            val photoPath = croppedImageUri?.let {
                                ImageHandler.saveImageToStorage(requireContext(), it)
                            }

                            val newUser = User(
                                uid = firebaseUser.uid,
                                name = name,
                                email = email,
                                photoUrl = photoPath
                            )

                            userDao.insertUser(newUser)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                                    putExtra("USER_EMAIL", email)
                                })
                                requireActivity().finish()
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
