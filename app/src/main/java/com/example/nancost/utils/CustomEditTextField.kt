package com.example.nancost.utils

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.nancost.R
import com.example.nancost.databinding.CustomEditTextBinding

class CustomEditTextField(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
        var uiTextFieldViewListener: UITextFieldViewListener? = null
        private var textErrorEmpty: String? = null
        private var textErrorInvalid: String? = null
        private var textErrorOrther: String? = null
        var componentType: Int = COMPONENT_ERROR
        private var binding: CustomEditTextBinding

        init {
            binding = CustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)
            background = ContextCompat.getDrawable(context, R.drawable.ic_bg_edittext_information)
            val ta = context.obtainStyledAttributes(attrs, R.styleable.UITextFieldView)
            if (ta.hasValue(R.styleable.UITextFieldView_textHint)) {
                binding.edtInput.hint = ta.getString(R.styleable.UITextFieldView_textHint)?.let {
                    HtmlCompat.fromHtml(
                        "<small><small>$it</small></small>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            }
            if (ta.hasValue(R.styleable.UITextFieldView_lengthMax)) {
                val maxLength = 10
                val filters = arrayOfNulls<InputFilter>(1)
                filters[0] = InputFilter.LengthFilter(maxLength)
                binding.edtInput.filters = filters
            }
            if (ta.hasValue(R.styleable.UITextFieldView_android_inputType)) {
                binding.edtInput.inputType = ta.getInt(
                    R.styleable.UITextFieldView_android_inputType,
                    EditorInfo.TYPE_TEXT_VARIATION_NORMAL
                )
            }
            if (ta.hasValue(R.styleable.UITextFieldView_android_imeOptions)) {
                binding.edtInput.imeOptions = ta.getInt(R.styleable.UITextFieldView_android_imeOptions, -1)
            }
            if (ta.hasValue(R.styleable.UITextFieldView_textColorError)) {
                binding.tvError.setTextColor(
                    ta.getColor(
                        R.styleable.TitleView_titleColor,
                        ContextCompat.getColor(context, android.R.color.holo_red_light)
                    )
                )
            }
            if (ta.hasValue(R.styleable.UITextFieldView_textErrorEmpty)) {
                textErrorEmpty = ta.getString(R.styleable.UITextFieldView_textErrorEmpty)
            }
            if (ta.hasValue(R.styleable.UITextFieldView_textErrorInvalid)) {
                textErrorInvalid = ta.getString(R.styleable.UITextFieldView_textErrorInvalid)
            }
            if (ta.hasValue(R.styleable.UITextFieldView_textErrorOrther)) {
                textErrorOrther = ta.getString(R.styleable.UITextFieldView_textErrorOrther)
            }
            binding.tvError.setOnClickListener {
                binding.tvError.visibility = View.GONE
                binding.edtInput.visibility = View.VISIBLE
            }

            binding.edtInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    uiTextFieldViewListener?.beforeTextChanged(s, start, count, after, componentType)
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    uiTextFieldViewListener?.onTextChanged(s, start, before, count, componentType)
                }

                override fun afterTextChanged(s: Editable) {
                    uiTextFieldViewListener?.afterTextChanged(s, componentType)
                }
            })
            ta.recycle()
        }

        fun getText(): String {
            return binding.edtInput.text.toString()
        }

        fun setText(string: String) {
            binding.edtInput.visibility = View.GONE
            binding.tv.visibility = View.VISIBLE
            binding.tv.text = string
        }

        fun setErrorMessage(errorCustom: String) {
            this@CustomEditTextField.textErrorOrther = errorCustom
        }

        fun textFieldNotEmpty(): Boolean {
            if (TextUtils.isEmpty(binding.edtInput.text.toString())) {
                return false
            }
            return true
        }

        fun setClickItemListener(listener: UITextFieldViewListener, componentType: Int) {
            this.uiTextFieldViewListener = listener
            this.componentType = componentType
        }

        fun setClickItemListener(listener: UITextFieldViewListener) {
            this.uiTextFieldViewListener = listener
        }

        interface UITextFieldViewListener {
            fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
                componentType: Int
            )

            fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int, componentType: Int)
            fun afterTextChanged(s: Editable, componentType: Int)
        }

        companion object {
            const val COMPONENT_ERROR = -1
        }
}