package com.example.nancost.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nancost.NancostApplication
import com.example.nancost.R
import com.example.nancost.databinding.FragmentUpdateBinding
import com.example.nancost.model.Nancost
import com.example.nancost.viewmodel.NancostViewModel
import com.example.nancost.viewmodel.NancostViewModelFactory

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NancostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this, NancostViewModelFactory(
            (requireActivity().application as NancostApplication).repository)
        )[NancostViewModel::class.java]

        binding.nameContent.setText(args.currentNancost.nancostName)
        binding.receivedVolumeContent.setText(args.currentNancost.receivedVolume.toString())
        binding.deliveredLeavesContent.setText(args.currentNancost.deliveredLeaves.toString())
        binding.deliveredVolumeContent.setText(args.currentNancost.deliveredVolume.toString())


        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.remainingVolumeContent.text = args.currentNancost.getRemainingVolume().toString()
        binding.unitPriceContent.text = args.currentNancost.unitPrice.toString()
        binding.amoutPayContent.text = args.currentNancost.getAmountPay().toString()
    }

    private fun updateItem() {
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
            val updateNancost = Nancost(
                args.currentNancost.id,
                nameContent,
                receivedVolumeContent.toString().toDouble(),
                deliveredLeavesContent.toString().toInt(),
                deliveredVolumeContent.toString().toDouble()
            )

            viewModel.updateNancost(updateNancost)
            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Hãy điền hết tất cả các trường...", Toast.LENGTH_LONG)
                .show()
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            deleteNancost()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNancost() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Có") { _, _ ->
            viewModel.deleteNancost(args.currentNancost.id)
            Toast.makeText(
                requireContext(),
                "Đã xóa: ${args.currentNancost.nancostName}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Không") { _, _ -> }
        builder.setTitle("Xóa ${args.currentNancost.nancostName}?")
        builder.setMessage("Bạn đã chắc chắn xóa ${args.currentNancost.nancostName} chưa?")
        builder.create().show()
    }

}