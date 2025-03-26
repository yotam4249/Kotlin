package com.example.kotlinproject.data.ui.viewmodel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.User
import android.widget.TextView
import android.widget.Toast

class HomeFragment : Fragment() {

    private val args: HomeFragmentArgs by navArgs() // SafeArgs generated class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val user: User = args.user!!

        // Example usage: show a toast or set a TextView
        Toast.makeText(requireContext(), "Welcome, ${user.name}", Toast.LENGTH_LONG).show()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = args.user
        // use the user object
    }
}
