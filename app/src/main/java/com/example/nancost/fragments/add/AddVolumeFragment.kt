package com.example.nancost.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nancost.R
import com.example.nancost.databinding.FragmentAddVolumeBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostData
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

class AddVolumeFragment : Fragment() {
    private var _binding: FragmentAddVolumeBinding? = null
    private val binding get() = _binding!!
    private var nancostDataList: ArrayList<NancostData?>? = arrayListOf()
    private val args by navArgs<AddVolumeFragmentArgs>()
    private var newPrice: Int? = 0
    private var volumeList: ArrayList<VolumeList?>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVolumeBinding.inflate(inflater, container, false)
        val view = binding.root
        args.currentNancost?.nancostName?.let { binding.nameContent.setText(it) }
        nancostDataList = args.currentNancost?.nancostDataList
        volumeList = args.nancostVolume?.volumeList
        newPrice = SharedPreUtils.getInt(AppConstant.Enum.NEW_PRICE, 0)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            if (binding.receivedVolumeContent.text.toString().isNotBlank()) {
                ActionDialog.show( childFragmentManager,
                    "Thêm khối lượng",
                    "Bạn có chắc chắn thêm ${binding.receivedVolumeContent.text.toString()} kg cho ${binding.nameContent.text.toString()}?"
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
        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)

        val receivedVolumeContent = binding.receivedVolumeContent.text.toString().toDouble()

        val volume = VolumeList(
            receivedVolume = receivedVolumeContent
        )
        volumeList?.add(volume)
        val nancostVolume = NancostVolume(
            nancostUid = args.currentNancost?.nancostUid,
            volumeList = volumeList
        )

        Firebase.database.getReference("$userUid/nancost/")
            .child("${args.currentNancost?.nancostUid}")
            .child("remainVolume")
            .setValue((args.currentNancost?.remainVolume?.plus(receivedVolumeContent))?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)?.toDouble())

        Firebase.database.getReference("$userUid/nancostVolume/")
            .child("${args.currentNancost?.nancostUid}")
            .setValue(nancostVolume)
            .addOnSuccessListener {
                Toast.makeText(context, "Đã thêm thành công!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_LONG).show()
            }

        lifecycleScope.launch {
            delay(1000)
            findNavController().navigate(R.id.action_addVolumeFragment_to_listFragment)
        }
    }
}