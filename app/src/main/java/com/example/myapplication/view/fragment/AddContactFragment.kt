package com.example.myapplication.view.fragment


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import com.example.contactapp.viewModel.ContactViewModel
import com.example.myapplication.view.activity.MainActivity

import com.example.myapplication.adapter.ContactAdapter
import com.example.myapplication.data.Contact


import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddContactBinding
import com.google.android.material.snackbar.Snackbar


class AddContactFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAddContactBinding
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter
    private var isPhone = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        contactAdapter = ContactAdapter()

        setupActionBar()


        binding.btnSave.setOnClickListener {
            addContact()
        }


//Start of changing the text field
        val spinner = binding.contactSpinnerType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.contact_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
//Finish
    }

    private fun setupActionBar() {
        requireActivity().setActionBar(binding.toolBar)

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().actionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel)

        binding.toolBar.setNavigationOnClickListener {

            findNavController().navigate(R.id.action_addContactFragment_to_allContactFragment)
        }
    }

    /**
     * to change the text field of the email and phone
     */

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

        if (parent?.getItemAtPosition(pos).toString() == "Email")
            binding.edPhone.apply {
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                binding.edPhonelbl.hint = "Email"
                isPhone = false
            }
        else
            binding.edPhone.apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                binding.edPhonelbl.hint = "Phone"
                isPhone = true
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    lateinit var newContact: Contact

    private fun addContact() {

        if (!binding.edName.text.isNullOrEmpty()) {
            if (!binding.edPhone.text.isNullOrEmpty()) {

                newContact = Contact(
                    0,
                    viewModel.setImageResForContact(binding.edName.text.toString()),
                    binding.edName.text.toString(),
                    binding.edCompany.text.toString(),
                    binding.edPhone.text.toString(),
                    binding.edCompany.text.toString(),
                    isPhone
                )
                viewModel.getAllContact().observe(viewLifecycleOwner) { allContact ->

                    if (!allContact.any { it.phoneOrEmail == newContact.phoneOrEmail }) {
                        if (!isPhone && isGmailAddress(binding.edPhone.text.toString().trim())) {
                            viewModel.upsertContact(
                                Contact(
                                    0,
                                    viewModel.setImageResForContact(binding.edName.text.toString()),
                                    binding.edName.text.toString(),
                                    binding.edCompany.text.toString(),
                                    binding.edPhone.text.toString(),
                                    binding.edCompany.text.toString(),
                                    isPhone
                                )
                            )
                            Toast.makeText(
                                requireContext(),
                                "Contact Saved Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().navigate(R.id.action_addContactFragment_to_allContactFragment)
                        } else if (isPhone && isPhoneNumber(
                                binding.edPhone.text.toString().trim()
                            )
                        ) {
                            viewModel.upsertContact(
                                Contact(
                                    0,
                                    viewModel.setImageResForContact(binding.edName.text.toString()),
                                    binding.edName.text.toString(),
                                    binding.edCompany.text.toString(),
                                    binding.edPhone.text.toString(),
                                    binding.edCompany.text.toString(),
                                    isPhone
                                )
                            )
                            Toast.makeText(
                                requireContext(),
                                "Contact Saved Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().navigate(R.id.action_addContactFragment_to_allContactFragment)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Please input phone or email valid",
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.snackBarError, null))
                                .show()
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Phone number or email is already taken",
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.snackBarError, null))
                            .show()
                    }
                }

            } else {
                Snackbar.make(
                    binding.root,
                    "Please Input Phone Or Email Of Contact !",
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.snackBarError, null))
                    .show()
            }

        } else {
            Snackbar.make(binding.root, "Please Input Name Of Contact !", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.snackBarError, null))
                .show()
        }

    }

    private fun isGmailAddress(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@gmail\\.com"
        return email.matches(Regex(pattern))
    }

    private fun isPhoneNumber(phoneNumber: String): Boolean {
        val pattern = "^(06|07|05)[0-9]{8}$|^(033)[0-9]{7}$"
        return phoneNumber.matches(Regex(pattern))
    }


}