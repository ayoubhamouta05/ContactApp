package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contact_table")
data class Contact (
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var image : Int ?=null,
    var name : String ,
    var company : String ?=null,
    var phoneOrEmail : String,
    var address : String?=null,
    var isPhone : Boolean
) : Serializable