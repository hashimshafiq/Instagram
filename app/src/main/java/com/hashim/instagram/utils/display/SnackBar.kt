package com.hashim.instagram.utils.display

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

object SnackBar {

    fun show(contextView: View, @StringRes text: Int, anchorView: View? = null){
        show(contextView, contextView.context.getString(text), anchorView)
    }

    fun showSuccess(contextView: View, @StringRes text: Int, anchorView: View? = null){
        showSuccess(contextView, contextView.context.getString(text), anchorView)
    }

    fun showError(contextView: View, @StringRes text: Int, anchorView: View? = null){
        showError(contextView, contextView.context.getString(text), anchorView)
    }

    fun show(contextView: View, text: String, anchorView: View? = null) {

        val snack = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(contextView.resources.getColor(android.R.color.white,null))
            setBackgroundTint(contextView.resources.getColor(android.R.color.black, null))
        }
        snack.anchorView = anchorView
        snack.show()
    }

    fun showSuccess(contextView: View, text: String, anchorView: View? = null) {

        val snack = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(contextView.resources.getColor(android.R.color.white,null))
            setBackgroundTint(contextView.resources.getColor(android.R.color.holo_green_dark, null))
        }
        snack.anchorView = anchorView
        snack.show()
    }

    fun showError(contextView: View, text: String, anchorView: View? = null) {

        val snack = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(contextView.resources.getColor(android.R.color.white,null))
            setBackgroundTint(contextView.resources.getColor(android.R.color.holo_red_light, null))
        }
        snack.anchorView = anchorView
        snack.show()
    }

}