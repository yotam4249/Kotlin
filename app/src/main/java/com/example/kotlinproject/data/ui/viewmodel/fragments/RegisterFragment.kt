package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kotlinproject.MainActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.dao.UserDao
import com.example.kotlinproject.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.kotlinproject.data.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var userDao: UserDao
    private var selectedImageUri: Uri? = null


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(uri).into(binding.ivAvatar)
        }
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
        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()

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

    private fun registerUser(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currUser = auth.currentUser
                    currUser?.let { user ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                val existingUser = userDao.getUserById(user.uid)
                                if (existingUser != null) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "User already exists! Please log in.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    return@launch
                                }

                                // Save image to storage (I/O operation)
                                val photoPath = selectedImageUri?.let { uri ->
                                    ImageHandler.saveImageToStorage(requireContext(), uri)
                                }

                                val newUser = User(
                                    uid = user.uid,
                                    name = name,
                                    email = email,
                                    photoUrl = photoPath
                                )

                                userDao.insertUser(newUser)

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Registration Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(requireContext(), MainActivity::class.java).apply {
                                            putExtra("USER_EMAIL", email)
                                        }
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Database Error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Registration Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

