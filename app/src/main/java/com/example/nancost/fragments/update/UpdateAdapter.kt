package com.example.nancost.fragments.update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.databinding.UpdateItemBinding
import com.example.nancost.model.NancostData

class UpdateAdapter : RecyclerView.Adapter<UpdateAdapter.MyViewHolder>() {
    private var nancostList = arrayListOf<NancostData?>()

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
                binding.index = position
                binding.tvDay.text = currentItem.dayAdded

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
        this.nancostList = nancostList
        notifyDataSetChanged()
    }
}