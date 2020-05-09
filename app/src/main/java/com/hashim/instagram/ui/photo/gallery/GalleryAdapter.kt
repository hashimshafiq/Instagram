package com.hashim.instagram.ui.photo.gallery

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.hashim.instagram.data.model.Image
import com.hashim.instagram.ui.base.BaseAdapter
import io.reactivex.subjects.PublishSubject

class GalleryAdapter(
    parentLifecycle: Lifecycle,
    images: ArrayList<Image>
) : BaseAdapter<Image,GalleryItemViewHolder>(parentLifecycle,images){

    object RxBus {
        val itemClickStream: PublishSubject<String> = PublishSubject.create()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder = GalleryItemViewHolder(parent)

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        RxBus.itemClickStream.onComplete()
    }
}