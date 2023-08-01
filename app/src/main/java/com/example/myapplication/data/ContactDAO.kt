package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.Contact

@Dao
interface ContactDAO {

    @Query("Select * From contact_table")
    fun getAllContact () : LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContact(contact : Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("Select * From contact_table where isPhone = 1")
    fun getPhoneContact () : LiveData<List<Contact>>

    @Query("Select * From contact_table where isPhone = 0")
    fun getEmailContact () : LiveData<List<Contact>>

    @Query("Select * From contact_table where name Like :name || '%'")
    fun searchAllContact(name: String):LiveData<List<Contact>>

    @Query("Select * From contact_table where name Like :name || '%' and isPhone = 1")
    fun searchPhoneContact(name: String) : LiveData<List<Contact>>

    @Query("Select * From contact_table where name Like :name || '%' and isPhone = 0")
    fun searchEmailContact(name: String) : LiveData<List<Contact>>

}