package com.mindorks.bootcamp.instagram.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Dummy(
    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String?
)