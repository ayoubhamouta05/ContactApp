package com.example.myapplication.view.activity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.example.myapplication.data.ContactDatabase
import com.example.myapplication.repository.ContactRepository
import com.example.contactapp.viewModel.ContactViewModel
import com.example.contactapp.viewModel.ContactViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.contactNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        val contactRepo = ContactRepository(ContactDatabase(this))
        val contactViewModelFactory = ContactViewModelProvider(application, contactRepo)

        viewModel = ViewModelProvider(this, contactViewModelFactory)[ContactViewModel::class.java]


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val navController = findNavController(R.id.contactNavHostFragment)

//        val fragment = supportFragmentManager.findFragmentById(R.id.contactNavHostFragment)
//        if (fragment is AddContactFragment && !fragment.isVisible) {
//            // The fragment is not visible, so finish the activity
//            finish()
//
//        } else {
        // The fragment is visible, handle the back button press normally
//            super.getOnBackPressedDispatcher().onBackPressed()
        if (!navController.popBackStack()) {
            finish()
        }
        //  }

    }




}