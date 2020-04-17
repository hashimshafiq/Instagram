package com.hashim.instagram.ui.photo.images

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.di.component.ViewHolderComponent
import com.hashim.instagram.ui.base.BaseItemViewHolder
import com.hashim.instagram.utils.display.ScreenUtils
import kotlinx.android.synthetic.main.item_view_grid_post.view.*
import kotlin.math.roundToInt


class ImageItemViewHolder(parent : ViewGroup):BaseItemViewHolder<Image,ImageItemViewModel>(R.layout.item_view_grid_post,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {
        itemView.ivPost.setOnClickListener {
            ImagesAdapter.RxBus.itemClickStream.onNext(viewModel.data.value!!.url)
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