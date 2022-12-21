package com.example.nancost.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nancost.R
import com.example.nancost.databinding.FragmentAddBinding
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private var nancostDataList: ArrayList<NancostData?>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            insertDataToDatabase()
        }
    }

    private fun insertDataToDatabase() {
        val nancostUid = UUID.randomUUID().toString()
        val nancostDataUid = UUID.randomUUID().toString()
        val nameContent = binding.nameContent.text.toString()
        val receivedVolumeContent = binding.receivedVolumeContent.text
        val deliveredLeavesContent = binding.deliveredLeavesContent.text
        val deliveredVolumeContent = binding.deliveredVolumeContent.text

        if (inputCheck(
                nameContent,
                receivedVolumeContent,
                deliveredLeavesContent,
                deliveredVolumeContent
            )
        ) {
            val nancostData = NancostData(
                nancostDataUid,
                nancostUid,
                receivedVolumeContent.toString().toDouble(),
                deliveredLeavesContent.toString().toInt(),
                deliveredVolumeContent.toString().toDouble()
            )
            nancostDataList?.add(nancostData)
            nancostData.getRemainingVolume()
            nancostData.getAmountPay()
            val nancost = Nancost(
                nancostUid,
                nameContent,
                nancostDataList
            )
            Firebase.database.getReference("nancost/")
                .child(nancostUid)
                .setValue(nancost)
                .addOnSuccessListener {
                    Toast.makeText(context, "Đã  thêm thành công!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_LONG).show()
                }
            lifecycleScope.launch {
                delay(1000)
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }
        }
    }

    private fun inputCheck(
        name: String,
        receivedVolume: Editable,
        deliveredLeaves: Editable,
        deliveredVolume: Editable
    ): Boolean {
        return !(TextUtils.isEmpty(name) && receivedVolume.isEmpty() && deliveredLeaves.isEmpty() && deliveredVolume.isEmpty())
    }
}