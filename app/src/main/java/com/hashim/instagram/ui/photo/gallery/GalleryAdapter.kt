package com.hashim.instagram.ui.photo.gallery

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.ui.base.BaseAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion

class GalleryAdapter(
    parentLifecycle: Lifecycle,
    images: ArrayList<Image>
) : BaseAdapter<Image,GalleryItemViewHolder>(parentLifecycle,images){

    object RxBus {
        val itemClickStream: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder = GalleryItemViewHolder(parent)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        RxBus.itemClickStream.onCompletion {  }
    }
}