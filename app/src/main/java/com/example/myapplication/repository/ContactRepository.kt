package com.example.myapplication.repository

import com.example.myapplication.data.Contact
import com.example.myapplication.data.ContactDatabase

class ContactRepository(
    private val db : ContactDatabase
) {

    fun getAllContact() = db.getContactDao().getAllContact()
    suspend fun upsertContact(contact: Contact) = db.getContactDao().upsertContact(contact)
    suspend fun deleteContact(contact: Contact) = db.getContactDao().deleteContact(contact)

    fun getPhoneContact() = db.getContactDao().getPhoneContact()
    fun getEmailContact() = db.getContactDao().getEmailContact()

    fun searchAllContact(name : String) = db.getContactDao().searchAllContact(name)
    fun searchPhoneContact(name: String) = db.getContactDao().searchPhoneContact(name)
    fun searchEmailContact(name: String) = db.getContactDao().searchEmailContact(name)

}