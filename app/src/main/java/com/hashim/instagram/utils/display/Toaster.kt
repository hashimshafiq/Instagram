package com.hashim.instagram.utils.display

import android.content.Context
import android.graphics.PorterDuff
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hashim.instagram.R

object Toaster {
    fun show(context: Context, text: CharSequence) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)
        toast.show()
    }
}