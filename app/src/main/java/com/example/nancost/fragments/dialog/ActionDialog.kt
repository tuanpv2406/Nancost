package com.example.nancost.fragments.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Toast
import com.example.nancost.databinding.FragmentActionDialogBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.nancost.R
import com.example.nancost.model.Price
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.FirebaseDatabase
import java.lang.IllegalStateException

class ActionDialog : DialogFragment() {
    private var binding: FragmentActionDialogBinding? = null

    private var title: String? = null
    private var message: String? = null
        set(value) {
            field = value
            binding?.message = value
        }
    private var isNewPrice: Boolean? = null

    var onPositiveActionListener: (() -> Unit)? = null
    var onNegativeActionListener: (() -> Unit)? = null

    override fun onResume() {
        super.onResume()
        try {
            val window: Window? = dialog?.window
            val size = Point()
            val display: Display? = window?.windowManager?.defaultDisplay
            display?.getSize(size)
            val width: Int = size.x
            window?.setLayout((width * 0.8).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window?.setGravity(Gravity.CENTER)
        }catch (_: Exception){}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        isCancelable = false
        title = arguments?.getString(KEY_TITLE)
        message = arguments?.getString(KEY_MESSAGE)
        isNewPrice = arguments?.getBoolean(KEY_IS_NEW_PRICE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val binding = FragmentActionDialogBinding.inflate(inflater)
        binding.buttonPositiveAction.setOnClickListener {
            onPositiveActionListener?.invoke()
            if (isNewPrice == true) {
                val price = Price(binding.newPriceContent.text.toString().toInt())
                FirebaseDatabase.getInstance().getReference("price/").setValue(price)
                    .addOnSuccessListener {
                        price.newPrice?.let { newPrice ->
                            SharedPreUtils.putInt(AppConstant.Enum.NEW_PRICE, newPrice)
                        }
                    }
                    .addOnFailureListener {
                    }
            }
        }
        binding.buttonNegativeAction.setOnClickListener { onNegativeActionListener?.invoke() }
        if (title.isNullOrEmpty()) title = null
        binding.title = title
        binding.message = message
        if (isNewPrice == true) {
            binding.buttonNegativeAction.text = "Hủy"
            binding.buttonPositiveAction.text = "Lưu"
        }
        this.binding = binding
        if (message.isNullOrBlank()) binding.textMessage.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isNewPrice?.let {
            if (it) {
                binding?.layoutNewPrice?.visibility = View.VISIBLE
            } else {
                binding?.layoutNewPrice?.visibility = View.GONE
            }
        } ?: run {
            binding?.layoutNewPrice?.visibility = View.GONE
        }
    }
    companion object {

        const val TAG = "action_dialog"

        private const val KEY_TITLE = "TITLE"
        private const val KEY_MESSAGE = "MESSAGE"
        private const val KEY_IS_NEW_PRICE = "KEY_IS_NEW_PRICE"

        fun show(
            fm: FragmentManager,
            title: String?,
            message: String?,
            isNewPrice: Boolean? = null
        ): ActionDialog {
            val prevFragment = fm.findFragmentByTag(TAG)
            if (prevFragment is ActionDialog) {
                prevFragment.dismiss()
                prevFragment.dismissAllowingStateLoss()
            }
            val fragment = newInstance(title, message, isNewPrice)
            try {
                fragment.show(fm, TAG)
            } catch (_: IllegalStateException) {
            }
            return fragment
        }

        private fun newInstance(
            title: String?,
            message: String?,
            isNewPrice: Boolean? = null
        ): ActionDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_MESSAGE, message)
            isNewPrice?.let {
                args.putBoolean(KEY_IS_NEW_PRICE, isNewPrice)
            }
            val fragment = ActionDialog()
            fragment.arguments = args
            return fragment
        }
    }
}