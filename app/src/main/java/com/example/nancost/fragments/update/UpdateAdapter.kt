package com.example.nancost.fragments.update

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.R
import com.example.nancost.databinding.UpdateItemBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.NancostData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateAdapter : RecyclerView.Adapter<UpdateAdapter.MyViewHolder>() {
    private var nancostList = listOf<NancostData?>()

    inner class MyViewHolder(val binding: UpdateItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UpdateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return nancostList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = nancostList[position]
        currentItem?.let {
            with(holder) {
                binding.tvDay.text = currentItem.dayAdded
                if (currentItem.amountPaid == null || currentItem.amountPaid == 0) {
                    binding.rowLayout.setBackgroundResource(R.drawable.bg_unpaid)
                } else {
                    if (((currentItem.receivedVolume ?: 0.0) > (currentItem.deliveredVolume ?: 0.0))
                        || (currentItem.amountWillPay ?: 0) > (currentItem.amountPaid ?: 0)) {
                        binding.rowLayout.setBackgroundResource(R.drawable.bg_not_enough_volume)
                    } else {
                        binding.rowLayout.setBackgroundResource(R.drawable.bg_paid)
                    }
                }
                binding.rowLayout.setOnClickListener {
                    val action =
                        DateUpdateFragmentDirections.actionDateUpdateFragmentToUpdateFragment(
                            currentItem
                        )
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }

    fun setData(nancostList: ArrayList<NancostData?>) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val result = nancostList.sortedBy {
            LocalDate.parse(it?.dayAdded, dateTimeFormatter)
        }

        this.nancostList = result
        notifyDataSetChanged()
    }

    fun removeNancostAt(position: Int, fragmentManager: FragmentManager, nancostDataList: ArrayList<NancostData?>) {
        ActionDialog.show(fragmentManager,
            "Xóa",
            "Bạn có chắc chắn xóa bản ghi này?"
        ).apply {
            var indexMatched = 0
            nancostDataList.forEachIndexed { index, item ->
                if (item?.nancostDataUid == nancostList[position]?.nancostDataUid) {
                    indexMatched = index
                }
            }
            onNegativeActionListener = {
                notifyDataSetChanged()
                dismiss()
            }
            onPositiveActionListener = {
                Firebase.database.getReference("nancost/${nancostList[position]?.nancostUid}/nancostDataList/$indexMatched")
                    .removeValue()
                nancostDataList.removeAt(position)
                nancostList = nancostDataList
                notifyDataSetChanged()
                dismiss()
                Toast.makeText(requireContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}