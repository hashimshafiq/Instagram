package com.hashim.instagram.ui.home

import com.hashim.instagram.data.model.Post


interface onClickListener {

    fun onClickPhoto()
    fun onLongClickPost(post: Post)
}