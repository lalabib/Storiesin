package com.project.lalabib.storiesin.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.project.lalabib.storiesin.R

class PasswordEditText: TextInputEditText {

    private var errorBackground: Drawable? = null
    private var defaultBackground: Drawable? = null
    private var isError: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bg = if (isError) {
            errorBackground
        } else {
            defaultBackground
        }
        background = bg
    }

    private fun init() {
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_default)
        errorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_error)

        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = s.toString()
                if (input.length < 6) {
                    error = context.getString(R.string.password_length)
                    isError = true
                } else {
                    isError = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length < 6) {
                    error = context.getString(R.string.password_length)
                    isError = true
                } else {
                    isError = false
                }
            }
        })
    }
}

