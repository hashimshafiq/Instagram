package com.mindorks.bootcamp.instagram.data.model

data class Image(
    val url: String,
    val headers: Map<String, String>,
    val placeholderWidth: Int = -1,
    val placeholderHeight: Int = -1
)