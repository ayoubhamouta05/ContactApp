package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Contact::class], version =1)

abstract  class ContactDatabase : RoomDatabase(){

   abstract fun getContactDao() : ContactDAO
    companion object {
        @Volatile
        var INSTANCE: ContactDatabase? = null

        private val LOCK = Any()
        operator fun invoke (context: Context) = INSTANCE ?: synchronized(LOCK){
            INSTANCE ?: createDatabase(context).also{
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            "tasks_table"
        ).fallbackToDestructiveMigration().build()
    }


}