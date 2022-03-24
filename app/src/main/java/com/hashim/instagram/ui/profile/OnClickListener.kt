package com.hashim.instagram.ui.profile

import android.widget.ImageView
import com.hashim.instagram.data.model.Image

interface OnClickListener {

    fun onClickPhoto(view: ImageView, image: Image)
}