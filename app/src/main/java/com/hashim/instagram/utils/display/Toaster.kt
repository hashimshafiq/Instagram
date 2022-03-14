package com.hashim.instagram.utils.display

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Toaster {
    fun show(contextView: View, text: CharSequence, anchorView: View? = null) {

        val snack = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(contextView.resources.getColor(android.R.color.white,null))
            setBackgroundTint(contextView.resources.getColor(android.R.color.black, null))
        }
        snack.anchorView = anchorView
        snack.show()
    }
}