package com.example.nancost.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.room.util.StringUtil
import com.example.nancost.R
import com.example.nancost.databinding.FragmentTotalBinding
import com.example.nancost.model.Nancost
import com.example.nancost.utils.StringUtils
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class TotalFragment : Fragment() {
    private var _binding: FragmentTotalBinding? = null
    private val binding get() = _binding!!
    val nancostList: ArrayList<Nancost?> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalBinding.inflate(inflater, container, false)

        Firebase.database.getReference("nancost/")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val nancost: Nancost? = ds.getValue(Nancost::class.java)
                            val nancostIterator = nancostList.iterator()
                            if (nancostIterator.hasNext()) {
                                if (nancostIterator.next()?.nancostUid == nancost?.nancostUid) return
                                else {
                                    nancostList.add(nancost)
                                }
                            } else {
                                nancostList.add(nancost)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.datePick.setOnClickListener {
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            picker.show(childFragmentManager, "tag")
            picker.addOnPositiveButtonClickListener {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                val instant = picker.selection?.let { selection -> Instant.ofEpochMilli(selection) }

                val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                binding.datePickerContent.setText(formatter.format(date))
                totalPerDay(formatter.format(date))
            }
            picker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            picker.addOnCancelListener {
                // Respond to cancel button click.
            }
            picker.addOnDismissListener {
                // Respond to dismiss events.
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_totalFragment_to_menuFragment)
            }
        })
    }

    private fun totalPerDay(date: String) {
        var totalAmountWillPay: Int? = 0
        var totalAmountPaid: Int? = 0
        var totalLeaves: Int? = 0
        var totalDeliveredVolume: Double? = 0.0
        var totalReceivedVolume: Double? = 0.0
        nancostList.forEach { nancost ->
            nancost?.nancostDataList?.forEach {
                if (it?.dayAdded.equals(date)) {
                    totalAmountWillPay = totalAmountWillPay?.plus(it?.amountWillPay?: 0)
                    totalAmountPaid = totalAmountPaid?.plus(it?.amountPaid?: 0)
                    totalLeaves = totalLeaves?.plus(it?.deliveredLeaves?: 0)
                    totalDeliveredVolume = totalDeliveredVolume?.plus(it?.receivedVolume?: 0.0)
                    totalReceivedVolume = totalReceivedVolume?.plus(it?.deliveredVolume?: 0.0)
                }
            }
        }
        binding.totalAmoutWillPay.text = StringUtils.formatCurrency(totalAmountWillPay ?: 0)
        binding.totalAmountPaid.text = StringUtils.formatCurrency(totalAmountPaid ?: 0)
        binding.totalLeaves.text = totalLeaves.toString()
        binding.totalDeliveredVolume.text = totalDeliveredVolume.toString()
        binding.totalReceivedVolume.text = totalReceivedVolume.toString()
    }
}