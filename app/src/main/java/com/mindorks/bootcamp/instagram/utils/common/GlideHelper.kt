package com.mindorks.bootcamp.instagram.utils.common

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

object GlideHelper {

    fun getProtectedUrl(url: String, headers: Map<String, String>): GlideUrl {
        val builder = LazyHeaders.Builder()
        for (entry in headers) builder.addHeader(entry.key, entry.value)
        return GlideUrl(url, builder.build())
    }
}
