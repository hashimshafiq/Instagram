package com.hashim.instagram.ui.profile.userposts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseAdapter
import com.hashim.instagram.ui.profile.OnClickListener

class UserPostAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post,UserPostItemViewHolder>(parentLifecycle,posts){

    lateinit var onClickListener : OnClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostItemViewHolder = UserPostItemViewHolder(parent, onClickListener)

    fun setupOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener

    }
}