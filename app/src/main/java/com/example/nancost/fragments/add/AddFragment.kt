package com.example.nancost.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nancost.R
import com.example.nancost.databinding.FragmentAddBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostVolume
import com.example.nancost.model.VolumeList
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private var newPrice: Int? = 0
    private var volumeList: ArrayList<VolumeList?>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        newPrice = SharedPreUtils.getInt(AppConstant.Enum.NEW_PRICE, 0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            if (
                inputCheck(
                    binding.nameContent.text.toString(),
                    binding.receivedVolumeContent.text.toString()
                )
            ) {
                ActionDialog.show( childFragmentManager,
                    "Thêm người mới",
                    "Bạn có chắc chắn thêm ${binding.nameContent.text.toString()}?"
                ).apply {
                    onNegativeActionListener = {
                        dismiss()
                    }
                    onPositiveActionListener = {
                        insertDataToDatabase()
                    }
                }
            } else {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.str_register_notice_msg)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }
        })
    }

    private fun insertDataToDatabase() {
        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)
        val nancostUid = UUID.randomUUID().toString()
        val nameContent = binding.nameContent.text.toString()
        val receivedVolumeContent = binding.receivedVolumeContent.text.toString()

        val nancost = Nancost(
            nancostUid = nancostUid,
            nancostName = nameContent,
            remainVolume = receivedVolumeContent.toDouble().toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        )
        val volume = VolumeList(
            receivedVolume = receivedVolumeContent.toDouble().toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        )
        volumeList?.add(volume)
        val nancostVolume = NancostVolume(
            nancostUid = nancostUid,
            volumeList = volumeList
        )
        Firebase.database.getReference("$userUid/nancost/")
            .child(nancostUid)
            .setValue(nancost)
            .addOnSuccessListener {
                Firebase.database.getReference("$userUid/nancostVolume/")
                    .child(nancostUid)
                    .setValue(nancostVolume)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Đã thêm thành công!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_LONG).show()
            }
        lifecycleScope.launch {
            delay(1000)
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }

    private fun inputCheck(name: String, receivedVolume: String): Boolean {
        return (name.isNotBlank() && receivedVolume.isNotBlank())
    }
}