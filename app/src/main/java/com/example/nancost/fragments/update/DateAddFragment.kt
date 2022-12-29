package com.example.nancost.fragments.update

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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
import com.example.nancost.utils.StringUtils
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
        binding.unitPriceContent.setText(newPrice.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deliveredLeavesContent.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    val amountPay = newPrice?.times(s.toString().toInt())
                    binding.amountPayContent.setText(amountPay.toString())
                } else {
                    binding.amountPayContent.setText("0")
                }

            }

        })

        binding.checkNancostPaid.setOnClickListener {
            if (binding.checkNancostPaid.isChecked) {
                binding.deliveredLeavesContent.setText("0")
                binding.amountPayContent.setText("0")
                binding.amountPaidContent.setText("0")
            }
        }

        binding.btnAdd.setOnClickListener {
            if (
                binding.deliveredLeavesContent.text.toString().isNotBlank() &&
                binding.deliveredVolumeContent.text.toString().isNotBlank() &&
                binding.amountPaidContent.text.toString().isNotBlank()
            ) {
                if ((args.currentNancost?.remainVolume?.minus(
                        binding.deliveredVolumeContent.text.toString().toDouble()
                    ) ?: 0.0) > 0.0
                ) {
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
                    binding.tvError.text = getString(R.string.str_warning_volume)
                }
            } else {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.str_register_notice_msg)
            }
        }
    }

    private fun insertDataToDatabase() {
        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)

        val nancostDataUid = UUID.randomUUID().toString()
        val deliveredLeaves = binding.deliveredLeavesContent.text.toString().toInt()
        val deliveredVolume = binding.deliveredVolumeContent.text.toString().toDouble()
        val amountPay = binding.amountPayContent.text.toString().toInt()
        val amountPaid = binding.amountPaidContent.text.toString().toInt()

        val nancostData = NancostData(
            nancostDataUid = nancostDataUid,
            nancostUid = args.currentNancost?.nancostUid,
            deliveredLeaves = deliveredLeaves,
            deliveredVolume = deliveredVolume,
            unitPrice = newPrice,
            amountWillPay = amountPay,
            amountPaid = amountPaid,
            checkNancostPaid = binding.checkNancostPaid.isChecked
        )
        nancostDataList?.add(nancostData)
        val nancost = Nancost(
            nancostUid = args.currentNancost?.nancostUid,
            nancostName = args.currentNancost?.nancostName,
            remainVolume = args.currentNancost?.remainVolume?.minus(deliveredVolume),
            nancostDataList = nancostDataList
        )

        Firebase.database.getReference("$userUid/nancost/")
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