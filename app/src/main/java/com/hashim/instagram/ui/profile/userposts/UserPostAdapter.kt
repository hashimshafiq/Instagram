package com.hashim.instagram.ui.profile.userposts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseAdapter

class UserPostAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post,UserPostItemViewHolder>(parentLifecycle,posts){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostItemViewHolder = UserPostItemViewHolder(parent)
}