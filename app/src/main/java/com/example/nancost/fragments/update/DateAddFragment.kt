package com.example.nancost.fragments.update

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nancost.R
import com.example.nancost.databinding.FragmentDateAddBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostData
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class DateAddFragment : Fragment() {
    private var _binding: FragmentDateAddBinding? = null
    private val binding get() = _binding!!
    private var nancostDataList: ArrayList<NancostData?>? = arrayListOf()
    private val args by navArgs<DateAddFragmentArgs>()
    private var newPrice: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateAddBinding.inflate(inflater, container, false)
        val view = binding.root
        args.currentNancost?.nancostName?.let { binding.nameContent.setText(it) }
        nancostDataList = args.currentNancost?.nancostDataList
        newPrice = SharedPreUtils.getInt(AppConstant.Enum.NEW_PRICE, 0)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            if (binding.receivedVolumeContent.text.toString().isNotBlank()) {
                ActionDialog.show( childFragmentManager,
                    "Thêm bản ghi mới",
                    "Bạn có chắc chắn thêm bản ghi mới cho ${binding.nameContent.text.toString()}?"
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
    }

    private fun insertDataToDatabase() {
        val nancostDataUid = UUID.randomUUID().toString()
        val receivedVolumeContent = binding.receivedVolumeContent.text.toString()

        val nancostData = NancostData(
            nancostDataUid,
            args.currentNancost?.nancostUid,
            receivedVolumeContent.toDouble(),
            unitPrice = newPrice
        )
        val remainingVolume = nancostData.getRemainingVolume()
        val amountWillPay = nancostData.getAmountPay()
        nancostData.remainVolume = remainingVolume
        nancostData.amountWillPay = amountWillPay
        nancostDataList?.add(nancostData)
        val nancost = Nancost(
            args.currentNancost?.nancostUid,
            args.currentNancost?.nancostName,
            nancostDataList
        )
        Firebase.database.getReference("nancost/")
            .child("${args.currentNancost?.nancostUid}")
            .setValue(nancost)
            .addOnSuccessListener {
                Toast.makeText(context, "Đã  thêm thành công!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_LONG).show()
            }
        lifecycleScope.launch {
            delay(1000)
            findNavController().navigate(R.id.action_dateAddFragment_to_listFragment)
        }
    }
}