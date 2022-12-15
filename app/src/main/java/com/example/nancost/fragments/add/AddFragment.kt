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
import com.example.nancost.NancostApplication
import com.example.nancost.R
import com.example.nancost.databinding.FragmentAddBinding
import com.example.nancost.databinding.FragmentListBinding
import com.example.nancost.model.Nancost
import com.example.nancost.viewmodel.NancostViewModel
import com.example.nancost.viewmodel.NancostViewModelFactory


class AddFragment : Fragment() {

    private lateinit var viewModel: NancostViewModel
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, NancostViewModelFactory(
            (requireActivity().application as NancostApplication).repository)
        )[NancostViewModel::class.java]
        binding.btnAdd.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val nameContent = binding.nameContent.text.toString()
        val receivedVolumeContent = binding.receivedVolumeContent.text
        val deliveredLeavesContent = binding.deliveredLeavesContent.text
        val deliveredVolumeContent = binding.deliveredVolumeContent.text

        if (inputCheck(nameContent, receivedVolumeContent, deliveredLeavesContent, deliveredVolumeContent)) {
            val nancost = Nancost(
                0,
                nameContent,
                receivedVolumeContent.toString().toDouble(),
                deliveredLeavesContent.toString().toInt(),
                deliveredVolumeContent.toString().toDouble()
            )
            viewModel.addNancost(nancost)
            Toast.makeText(context, "Đã  thêm thành công!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }

    private fun inputCheck(name: String, receivedVolume: Editable, deliveredLeaves: Editable, deliveredVolume: Editable): Boolean {
        return !(TextUtils.isEmpty(name) && receivedVolume.isEmpty() && deliveredLeaves.isEmpty() && deliveredVolume.isEmpty())
    }

}