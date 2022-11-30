package com.example.nancost.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nancost.R
import com.example.nancost.viewmodel.NancostViewModel


class AddFragment : Fragment() {

    private lateinit var viewModel: NancostViewModel
    private lateinit var name: EditText
    private lateinit var receivedVolume: EditText
    private lateinit var deliveredLeaves: EditText
    private lateinit var deliveredVolume: EditText
    private lateinit var btnAdd: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        name = view.findViewById(R.id.name_content)
        receivedVolume = view.findViewById(R.id.received_volume_content)
        deliveredLeaves = view.findViewById(R.id.delivered_leaves_content)
        deliveredVolume = view.findViewById(R.id.delivered_volume_content)

        viewModel = ViewModelProvider(this)[NancostViewModel::class.java]
        btnAdd.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase(){
        val nameContent = name.text.toString()
        val receivedVolumeContent = receivedVolume.text
        val deliveredVolumeContent = deliveredVolume.text

        if(inputCheck(fName,lName, ag)){
//            Create User object
        val user = User(0,fName,lName,Integer.parseInt(ag.toString()))
//            Add data to Database
            viewModel.addUser(user)
            Toast.makeText(context,"Successfully added",Toast.LENGTH_LONG).show()
//            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }

    private fun inputCheck(firstName: String,lastName: String, age: Editable): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

}