package com.android.daily.utilities.extenstions

import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

/**
 * Use this Extension fun to clear the error set when text changes.
 */
fun TextInputLayout.clearErrorOnTextChange() {
    editText?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isErrorEnabled = false
        }

    })
}