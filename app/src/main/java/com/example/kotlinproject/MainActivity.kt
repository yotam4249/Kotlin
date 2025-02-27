package com.example.kotlinproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.kotlinproject.data.dao.UserDao
import com.example.kotlinproject.data.local.AppDatabase
import com.example.kotlinproject.data.model.User
import com.example.kotlinproject.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var userEmail:String? = null
    private lateinit var userDao : UserDao
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userEmail = intent.getStringExtra("USER_EMAIL")
        var myUser : User? = null
        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        lifecycleScope.launch {
            myUser = withContext(Dispatchers.IO){
                userDao.getByEmail(userEmail ?: "")
            }

            if(myUser != null){
                Toast.makeText(this@MainActivity,"Hello ${myUser!!.name}",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@MainActivity,"User not found!",Toast.LENGTH_LONG).show()
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


    }
}
