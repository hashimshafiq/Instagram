package com.hashim.instagram.ui.home.post.likeduser

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseAdapter

class LikedUserAdapter(
    parentLifecycle: Lifecycle,
    users: ArrayList<Post.User>
) : BaseAdapter<Post.User,LikedUserItemViewHolder>(parentLifecycle,users) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LikedUserItemViewHolder(parent)




}