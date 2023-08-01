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
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.view.activity.MainActivity
import com.example.myapplication.data.Contact
import com.example.contactapp.viewModel.ContactViewModel
import com.example.myapplication.databinding.FragmentModifyContactBinding
import com.google.android.material.snackbar.Snackbar

class ModifyContactFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentModifyContactBinding
    private lateinit var viewModel: ContactViewModel
    private val args: ModifyContactFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifyContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val contact = args.contact
        binding.edModifyName.setText(contact.name)
        binding.edModifyPhone.setText(contact.phoneOrEmail)

        binding.btnCancel.setOnClickListener {
            //findNavController().navigate(R.id.action_modifyContactFragment_to_allContactFragment)
            findNavController().popBackStack()
        }
        binding.btnModify.setOnClickListener {
            updateContact()
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

    /**
     * to change the text field of the email and phone
     */

    private var checkTypeContact = true
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val contact = args.contact

        if (checkTypeContact) {
            if (!contact.isPhone)
                binding.contactSpinnerType.setSelection(0)
            else
                binding.contactSpinnerType.setSelection(1)
            checkTypeContact = false
        }

        if (parent?.getItemAtPosition(pos).toString() == "Email") {
            contact.isPhone = false
            binding.edPhonelbl.hint = "Email"
            binding.edModifyPhone.apply {
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        } else {
            contact.isPhone = true
            binding.edPhonelbl.hint = "Phone"
            binding.edModifyPhone.apply {
                inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun updateContact() {
        val contact = args.contact

        if (!binding.edModifyName.text.isNullOrEmpty()) {
            if (!binding.edModifyPhone.text.isNullOrEmpty()) {
                viewModel.upsertContact(
                    Contact(
                        contact.id,
                        viewModel.setImageResForContact(binding.edModifyName.text.toString()) ,
                        binding.edModifyName.text.toString(),
                        binding.edModifyCompany.text.toString(),
                        binding.edModifyPhone.text.toString(),
                        binding.edModifyAddress.text.toString(),
                        contact.isPhone
                    )
                )
                Toast.makeText(
                    requireActivity(),
                    "Contact Updated Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_modifyContactFragment_to_allContactFragment)
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
}