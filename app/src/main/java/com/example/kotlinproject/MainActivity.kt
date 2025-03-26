
package com.example.kotlinproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.kotlinproject.data.dao.UserDao
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.NavController
import com.example.kotlinproject.NavGraphDirections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var userEmail: String? = null
    private lateinit var userDao: UserDao

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        userEmail = intent.getStringExtra("USER_EMAIL")
        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        lifecycleScope.launch {
            val myUser = withContext(Dispatchers.IO) {
                userDao.getByEmail(userEmail ?: "")
            }

            if (myUser != null) {
                Toast.makeText(this@MainActivity, "Hello ${myUser.name}", Toast.LENGTH_LONG).show()

                // Navigate to default destination manually with SafeArgs
                val action = NavGraphDirections.actionGlobalHomeFragment(myUser)
                navController.navigate(action)

                // Set up BottomNavigationView with SafeArgs handling
                setupBottomNavWithUser(myUser)
            } else {
                Toast.makeText(this@MainActivity, "User not found!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupBottomNavWithUser(user: User) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentDestId = navController.currentDestination?.id

            val action = when (item.itemId) {
                R.id.homeFragment -> {
                    if (currentDestId != R.id.homeFragment)
                        NavGraphDirections.actionGlobalHomeFragment(user)
                    else null
                }
                R.id.profileFragment -> {
                    if (currentDestId != R.id.profileFragment)
                        NavGraphDirections.actionGlobalProfileFragment(user)
                    else null
                }
                R.id.addPostFragment -> {
                    if (currentDestId != R.id.addPostFragment)
                        NavGraphDirections.actionGlobalAddPostFragment(user)
                    else null
                }
                R.id.searchFragment -> {
                    if (currentDestId != R.id.searchFragment)
                        NavGraphDirections.actionGlobalSearchFragment(user) // ✅ Add this
                    else null
                }
                else -> null
            }

            action?.let {
                navController.navigate(it)
                true
            } ?: false
        }
    }
}
