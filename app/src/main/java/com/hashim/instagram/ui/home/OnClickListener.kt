package com.hashim.instagram.ui.home

import android.widget.ImageView
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.data.model.Post


interface OnClickListener {

    fun onClickProfilePhoto()
    fun onLongClickPost(post: Post)
    fun onClickPhoto(view: ImageView, image: Image)

}