package com.imahdev.myappstory.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EditTextEmail : AppCompatEditText{

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
               //...
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                    setError("Email harus alamat email valid", null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //.....
            }

        })



    }


}