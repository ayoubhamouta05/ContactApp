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
import com.example.myapplication.databinding.FragmentAllContactBinding
import com.example.myapplication.view.activity.MainActivity


class AllContactFragment : Fragment() {

    lateinit var binding: FragmentAllContactBinding
    lateinit var contactAdapter: ContactAdapter
    private lateinit var viewModel: ContactViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllContactBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        getContacts()

        contactAdapter.setOnLongItemClickListener {
            deleteOrModifyContact(it)
        }

        binding.fabDeleteContact.setOnClickListener {
            for (i in contactAdapter.deleteContactList)
                viewModel.deleteContact(i)
        }

        binding.cvAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_allContactFragment_to_addContactFragment)
        }

        binding.edSearch.addTextChangedListener { name ->
            viewModel.searchAllContact(name.toString()).observe(viewLifecycleOwner) { searchList ->
                contactAdapter.list = searchList
            }
        }

        binding.cvDeleteContact.setOnClickListener {
            if(contactAdapter.list.isEmpty()){
                Toast.makeText(requireContext(), "There Is No Contact", Toast.LENGTH_SHORT).show()
            }else
            selectMultipleContacts()
        }

    }


    private fun setupAdapter() {
        contactAdapter = ContactAdapter()
        binding.rvAllContact.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun getContacts() {
        viewModel.getAllContact().observe(viewLifecycleOwner) { allContact ->
            contactAdapter.list = allContact
        }
        setupAdapter()
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
                R.id.action_allContactFragment_to_modifyContactFragment,
                bundle
            )

            myDialog.dismiss()
        }
        myDialog.show()

    }

    private fun selectMultipleContacts(){
        viewModel.DELETE_MODE.postValue(!viewModel.DELETE_MODE.value!!)
        viewModel.getAllContact().observe(viewLifecycleOwner){contactList->
            if (contactList.isEmpty()){
                binding.fabDeleteContact.visibility = View.GONE
            }
            else {
                binding.fabDeleteContact.visibility = View.VISIBLE
                viewModel.DELETE_MODE.observe(viewLifecycleOwner) {
                    contactAdapter.setDeleteMode(it)
                    if (it) {
                        binding.fabDeleteContact.visibility = View.VISIBLE
                    } else {
                        binding.fabDeleteContact.visibility = View.GONE
                    }
                }
            }
        }

    }


}