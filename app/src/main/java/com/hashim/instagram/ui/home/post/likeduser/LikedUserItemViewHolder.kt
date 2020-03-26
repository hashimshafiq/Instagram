package com.hashim.instagram.ui.home.post.likeduser

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.utils.common.GlideHelper
import kotlinx.android.synthetic.main.item_view_post.view.*

class LikedUserItemViewHolder(parent: ViewGroup) : BaseItemViewHolder<Post.User,LikedUserItemViewModel>(R.layout.item_view_liked_user,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            itemView.tvName.text = it
        })



        viewModel.profileImage.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = itemView.ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    itemView.ivProfile.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(itemView.ivProfile)
            }
        })

    }

    override fun setupView(view: View) {}
}