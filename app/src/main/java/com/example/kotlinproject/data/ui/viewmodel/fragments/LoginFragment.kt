package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kotlinproject.MainActivity
import com.example.kotlinproject.R
import com.example.kotlinproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val loggedUser = auth.currentUser
        if(loggedUser != null){
            val intent = Intent(requireContext(),MainActivity::class.java)
                .putExtra("USER_EMAIL",loggedUser.email)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginUser(email, password)
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginUser(email: String, password: String) {
        if(email == "" || password == "")
        {
            Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show()
        }
        else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                            .putExtra("USER_EMAIL", email)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
