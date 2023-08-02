package com.example.myapplication.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.viewModel.ContactViewModel
import com.example.myapplication.R
import com.example.myapplication.adapter.ContactAdapter
import com.example.myapplication.data.Contact
import com.example.myapplication.databinding.DeleteUpdateContactDialogBinding
import com.example.myapplication.databinding.FragmentEmailContactBinding
import com.example.myapplication.view.activity.MainActivity


class EmailContactFragment : Fragment() {

    lateinit var binding: FragmentEmailContactBinding
    private lateinit var viewModel : ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailContactBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        getEmailContact()

        contactAdapter.setOnLongItemClickListener {
            deleteOrModifyContact(it)
        }

        binding.fabDeleteEmailContact.setOnClickListener {
            for (i in contactAdapter.deleteContactList)
                viewModel.deleteContact(i)
        }
        binding.cvAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_emailContactFragment_to_addContactFragment)
        }

        binding.cvDeleteContact.setOnClickListener {
            if(contactAdapter.list.isEmpty()){
                Toast.makeText(requireContext(), "There Is No Contact", Toast.LENGTH_SHORT).show()
            }else
                selectMultipleContacts()
        }

        binding.edEmailSearch.addTextChangedListener {name->
            viewModel.searchEmailContact(name.toString()).observe(viewLifecycleOwner){emailList->
                contactAdapter.list = emailList
            }
        }
    }

    private fun setupRecyclerView(){
        contactAdapter = ContactAdapter()
        binding.rvEmailContact.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }

    private fun getEmailContact(){
        viewModel.getEmailContact().observe(viewLifecycleOwner){emailContact->
            contactAdapter.list = emailContact
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
                R.id.action_emailContactFragment_to_modifyContactFragment,
                bundle
            )

            myDialog.dismiss()
        }
        myDialog.show()

    }
    private fun selectMultipleContacts(){
        viewModel.DELETE_MODE.postValue(!viewModel.DELETE_MODE.value!!)
        viewModel.getEmailContact().observe(viewLifecycleOwner){contactList->
            if (contactList.isEmpty()){
                binding.fabDeleteEmailContact.visibility = View.GONE

            }
            else {
                binding.fabDeleteEmailContact.visibility = View.VISIBLE
                viewModel.DELETE_MODE.observe(viewLifecycleOwner) {
                    contactAdapter.setDeleteMode(it)
                    if (it) {
                        binding.fabDeleteEmailContact.visibility = View.VISIBLE
                    } else {
                        binding.fabDeleteEmailContact.visibility = View.GONE
                    }
                }
            }
        }

    }

}