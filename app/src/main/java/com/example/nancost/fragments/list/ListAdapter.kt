package com.example.nancost.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.R
import com.example.nancost.databinding.RowItemBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostVolume
import com.example.nancost.model.VolumeList
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var nancostList = arrayListOf<Nancost?>()
    private var volumeList = arrayListOf<NancostVolume?>()

    inner class MyViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return nancostList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = nancostList[position]
        val currentVolume = volumeList.find { it?.nancostUid == currentItem?.nancostUid }
        currentItem?.let {
            with(holder) {
                binding.name.text = currentItem.nancostName
                binding.remainingVolume.text = "Khối lượng còn lại: ${currentItem.remainVolume} kg"
                binding.layoutContent.setOnClickListener {
                    val action =
                        ListFragmentDirections.actionListFragmentToDateUpdateFragment(currentItem)
                    holder.itemView.findNavController().navigate(action)
                }
                binding.btnAdd.setOnClickListener {
                    val action =
                        ListFragmentDirections.actionListFragmentToAddVolumeFragment(currentItem, currentVolume)
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }

    fun setData(nancostList: ArrayList<Nancost?>, volumeList: ArrayList<NancostVolume?>? = null) {
        this.nancostList = nancostList
        if (volumeList != null) {
            this.volumeList = volumeList
        }
        notifyDataSetChanged()
    }

    fun removeNancostAt(position: Int, fragmentManager: FragmentManager, userUid: String?) {
        ActionDialog.show(fragmentManager,
            "Xóa",
            "Bạn có chắc chắn xóa ${nancostList[position]?.nancostName}?"
        ).apply {
            onNegativeActionListener = {
                notifyDataSetChanged()
                dismiss()
            }
            onPositiveActionListener = {
                Firebase.database.getReference("$userUid/nancost/${nancostList[position]?.nancostUid}")
                    .removeValue()

                Firebase.database.getReference("$userUid/nancostVolume/${nancostList[position]?.nancostUid}")
                    .removeValue()

                nancostList.removeAt(position)
                notifyDataSetChanged()
                dismiss()
                Toast.makeText(requireContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}