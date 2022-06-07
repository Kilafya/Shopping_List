package com.kilafyan.shoppinglist.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kilafyan.shoppinglist.R

interface ResetErrorMessage {
    fun resetErrorMessage()
}

class TextWatcherImpl(private val resetErrorMessage: ResetErrorMessage): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        resetErrorMessage.resetErrorMessage()
    }

    override fun afterTextChanged(s: Editable?) {}
}

@BindingAdapter("errorInputName")
fun bindSetErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun bindSetErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("resetError")
fun bindResetError(editText: TextInputEditText, resetFunction: ResetErrorMessage) {
    editText.addTextChangedListener(TextWatcherImpl(resetFunction))
}