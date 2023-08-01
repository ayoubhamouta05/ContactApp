package com.example.contactapp.viewModel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.ContactRepository

class ContactViewModelProvider(
    val app : Application,
    private val contactRepo : ContactRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(app,contactRepo) as T
    }
}