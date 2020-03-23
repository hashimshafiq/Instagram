package com.hashim.instagram.ui.home.post

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseAdapter
import com.hashim.instagram.ui.home.onClickListener

class PostsAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post,PostItemViewHolder>(parentLifecycle,posts) {

    lateinit var onClickListener : onClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostItemViewHolder(parent,onClickListener)

    fun setupOnClickListener(onClickListener: onClickListener){
            this.onClickListener = onClickListener

    }


}