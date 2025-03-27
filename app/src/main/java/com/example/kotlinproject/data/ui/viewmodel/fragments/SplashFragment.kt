
package com.example.kotlinproject.ui
//package com.example.kotlinproject.data.ui.viewmodel.fragments
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kotlinproject.R
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireContext()
        val userEmail = requireActivity().intent.getStringExtra("USER_EMAIL")

        lifecycleScope.launch {
            // Optional: short delay for splash effect
            delay(1500)

            val user = withContext(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(context)
                db.userDao().getByEmail(userEmail ?: "")
            }

            if (user != null && findNavController().currentDestination?.id == R.id.splashFragment) {
                val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment(user)
                findNavController().navigate(action)
            } else {
                // Optional: navigate to loginFragment if user is null
                // findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }
}
