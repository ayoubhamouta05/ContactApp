package com.example.myapplication.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.viewModel.ContactViewModel
import com.example.myapplication.R


import com.example.myapplication.data.Contact
import com.example.myapplication.databinding.FragmentAddContactBinding
import com.example.myapplication.databinding.FragmentAllContactBinding
import com.example.myapplication.databinding.ItemContactBinding
import com.example.myapplication.view.activity.MainActivity


class ContactAdapter() : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    private var deleteMode =false

    fun setDeleteMode(enabled: Boolean) {
        deleteMode = enabled
        notifyDataSetChanged()
    }

    var deleteContactList = mutableListOf<Contact>()
    private val differCallback = object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, differCallback)
    var list: List<Contact>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        holder.binding.apply {
            list.image?.let { contactImg.setImageResource(list.image!!) }
            contactName.text = list.name
            contactNumber.text = list.phoneOrEmail
            if (list.isPhone) {
                callImg.apply {
                    setImageResource(R.drawable.ic_phone)
                    setOnClickListener {
                        val uri: Uri = Uri.parse("tel:${contactNumber.text}")
                        val intent = Intent(Intent.ACTION_DIAL, uri)
                        context.startActivity(intent)
                    }
                }

            } else {
                callImg.apply {
                    setImageResource(R.drawable.ic_email)
                    setOnClickListener {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("mailto:" + "${contactNumber.text}")
                            )
                        )
                    }
                }
            }

            root.apply {
                setOnLongClickListener {
                    onLongItemClickListener?.let { it(list) }
                    onUpdateBtnClickListener?.let { it(list) }
                    false
                }
            }

            if (deleteMode){
                checkDelete.visibility = View.VISIBLE
                callImg.visibility = View.GONE
            }else{
                callImg.visibility = View.VISIBLE
                checkDelete.visibility = View.GONE

            }

            checkDelete.apply {
                setOnClickListener {
                    if (this.isChecked){
                        deleteContactList.add(list)
                    }else{
                        deleteContactList.remove(list)
                    }
                    onDeleteClickListener?.let { it(list) }
                }
            }
        }
    }

    private var onLongItemClickListener: ((Contact) -> Unit)? = null

    fun setOnLongItemClickListener(listener: (Contact) -> Unit) {
        onLongItemClickListener = listener
    }

    private var onDeleteClickListener : ((Contact )-> Unit)? = null

    private var onUpdateBtnClickListener: ((Contact) -> Unit)? = null

}