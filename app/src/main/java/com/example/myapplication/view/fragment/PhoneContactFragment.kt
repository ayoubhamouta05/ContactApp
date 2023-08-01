package com.example.myapplication.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.view.activity.MainActivity
import com.example.myapplication.adapter.ContactAdapter
import com.example.myapplication.data.Contact

import com.example.contactapp.viewModel.ContactViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.DeleteUpdateContactDialogBinding
import com.example.myapplication.databinding.FragmentPhoneContactBinding


class PhoneContactFragment : Fragment() {

    lateinit var binding: FragmentPhoneContactBinding
    private lateinit var viewModel : ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()
        getPhoneContact()

        contactAdapter.setOnLongItemClickListener {
            deleteOrModifyContact(it)
        }

        binding.fabDeletePhoneContact.setOnClickListener {
            for (i in contactAdapter.deleteContactList)
                viewModel.deleteContact(i)
        }
        binding.cvAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_phoneContactFragment_to_addContactFragment)
        }
        binding.cvDeleteContact.setOnClickListener {
            if(contactAdapter.list.isEmpty()){
                Toast.makeText(requireContext(), "There Is No Contact", Toast.LENGTH_SHORT).show()
            }else
                selectMultipleContacts()

        }
        binding.edPhoneSearch.addTextChangedListener {name->
            viewModel.searchPhoneContact(name.toString()).observe(viewLifecycleOwner){phoneList->
                contactAdapter.list = phoneList
            }
        }

    }
    private fun setupRecyclerView(){
        contactAdapter = ContactAdapter(viewModel)
        binding.rvPhoneContact.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getPhoneContact(){
        viewModel.getPhoneContact().observe(viewLifecycleOwner){phoneContact->
            contactAdapter.list = phoneContact
        }
    }
    private fun deleteOrModifyContact(contact: Contact) {
        val dialogBinding = DeleteUpdateContactDialogBinding.inflate(layoutInflater)
        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogBinding.root)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable._solid_15dp_fabcolor,null))
        dialogBinding.deleteBtn.setOnClickListener {
            viewModel.deleteContact(
                Contact(
                    contact.id,
                    contact.image,
                    contact.company!!,
                    contact.name,
                    contact.phoneOrEmail,
                    contact.address,
                    contact.isPhone
                )

            )
            myDialog.dismiss()
            Toast.makeText(requireContext(), "Contact Deleted Successfully", Toast.LENGTH_SHORT)
                .show()
        }
        dialogBinding.updateBtn.setOnClickListener {

            val bundle = Bundle().apply {
                putSerializable("contact", contact)
            }
            findNavController().navigate(
                R.id.action_phoneContactFragment_to_modifyContactFragment,
                bundle
            )

            myDialog.dismiss()
        }
        myDialog.show()
    }
    private fun selectMultipleContacts(){
        viewModel.DELETE_MODE.postValue(!viewModel.DELETE_MODE.value!!)
        viewModel.getPhoneContact().observe(viewLifecycleOwner){contactList->
            if (contactList.isEmpty()){
                binding.fabDeletePhoneContact.visibility = View.GONE
            }
            else {
                binding.fabDeletePhoneContact.visibility = View.VISIBLE
                viewModel.DELETE_MODE.observe(viewLifecycleOwner) {
                    contactAdapter.setDeleteMode(it)
                    if (it) {
                        binding.fabDeletePhoneContact.visibility = View.VISIBLE
                    } else {
                        binding.fabDeletePhoneContact.visibility = View.GONE
                    }
                }
            }
        }

    }


}