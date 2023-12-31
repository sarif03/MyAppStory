package com.imahdev.myappstory.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditTextPassword : AppCompatEditText {
    constructor(context: Context): super(context) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context,attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()

    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.toString().length < 8) {
                    setError("Minimal password 8 character", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }
}