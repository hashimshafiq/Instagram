package com.hashim.instagram.ui.photo.gallery

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import kotlinx.android.synthetic.main.item_view_grid_post.view.*


class GalleryItemViewHolder(parent : ViewGroup):BaseItemViewHolder<Image,GalleryItemViewModel>(R.layout.item_view_grid_post,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {
        itemView.ivPost.setOnClickListener {
            GalleryAdapter.RxBus.itemClickStream.onNext(viewModel.data.value!!.url)
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.imageDetail.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.ivPost.context)
                    .load(url)

                glideRequest.into(itemView.ivPost)

            }
        })
    }




}