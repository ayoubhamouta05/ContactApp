package com.example.contactapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.Contact
import com.example.myapplication.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(
    app: Application,
    private var contactRepo: ContactRepository
) : AndroidViewModel(app) {
    var DELETE_MODE : MutableLiveData<Boolean> = MutableLiveData(false)

    fun getAllContact() = contactRepo.getAllContact()

    fun upsertContact(contact: Contact) = viewModelScope.launch {
        contactRepo.upsertContact(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        contactRepo.deleteContact(contact)
    }

    fun getPhoneContact() = contactRepo.getPhoneContact()

    fun getEmailContact() = contactRepo.getEmailContact()

    fun searchAllContact(name: String) = contactRepo.searchAllContact(name)

    fun searchPhoneContact(name: String) = contactRepo.searchPhoneContact(name)

    fun searchEmailContact(name: String) = contactRepo.searchEmailContact(name)


    fun setImageResForContact(name: String): Int? {
        return when (name.first()) {
            'a', 'A' -> R.drawable.a_img
            'b', 'B' -> R.drawable.b_img
            'c', 'C' -> R.drawable.c_img
            'd', 'D' -> R.drawable.d_img
            'e', 'E' -> R.drawable.e_img
            'f', 'F' -> R.drawable.f_img
            'g', 'G' -> R.drawable.g_img
            'h', 'H' -> R.drawable.h_img
            'i', 'I' -> R.drawable.i_img
            'j', 'J' -> R.drawable.j_img
            'k', 'K' -> R.drawable.k_img
            'l', 'L' -> R.drawable.l_img
            'm', 'M' -> R.drawable.m_img
            'n', 'N' -> R.drawable.n_img
            'o', 'O' -> R.drawable.o_img
            'p', 'P' -> R.drawable.p_img
            'q', 'Q' -> R.drawable.q_img
            'r', 'R' -> R.drawable.r_img
            's', 'S' -> R.drawable.s_img
            't', 'T' -> R.drawable.t_img
            'u', 'U' -> R.drawable.u_img
            'v', 'V' -> R.drawable.v_img
            'w', 'W' -> R.drawable.w_img
            'x', 'X' -> R.drawable.x_img
            'y', 'Y' -> R.drawable.y_img
            'z', 'Z' -> R.drawable.z_img
            else -> null
        }
    }
}