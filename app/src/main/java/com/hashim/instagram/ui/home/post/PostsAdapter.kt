package com.hashim.instagram.ui.home.post

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.ui.base.BaseAdapter
import com.hashim.instagram.ui.home.OnClickListener

class PostsAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post,PostItemViewHolder>(parentLifecycle,posts) {

    lateinit var OnClickListener : OnClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostItemViewHolder(parent,OnClickListener)

    fun setupOnClickListener(OnClickListener: OnClickListener){
            this.OnClickListener = OnClickListener

    }


}