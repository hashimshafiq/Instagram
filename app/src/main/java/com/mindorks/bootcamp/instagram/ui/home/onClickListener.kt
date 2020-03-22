package com.mindorks.bootcamp.instagram.ui.home

import com.mindorks.bootcamp.instagram.data.model.Post

interface onClickListener {

    fun onClickPhoto()
    fun onLongClickPost(post: Post)
}