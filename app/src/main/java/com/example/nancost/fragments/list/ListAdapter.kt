package com.example.nancost.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.databinding.RowItemBinding
import com.example.nancost.model.Nancost

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var nancostList = arrayListOf<Nancost?>()

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
        currentItem?.let {
            with(holder) {
                binding.name.text = currentItem.nancostName
                binding.numberRecord.text = "${currentItem.nancostDataList?.size} bản ghi"
                binding.rowLayout.setOnClickListener {
                    val action =
                        ListFragmentDirections.actionListFragmentToDateUpdateFragment(currentItem)
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }

    fun setData(nancostList: ArrayList<Nancost?>) {
        this.nancostList = nancostList
        notifyDataSetChanged()
    }
}