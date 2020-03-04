package com.mindorks.bootcamp.instagram.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Post(
    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("imgUrl")
    val imageUrl: String,

    @Expose
    @SerializedName("imgWidth")
    val imageWidth: Int?,

    @Expose
    @SerializedName("imgHeight")
    val imageHeight: Int?,

    @Expose
    @SerializedName("user")
    val creator: User,

    @Expose
    @SerializedName("likedBy")
    val likedBy: MutableList<User>?,

    @Expose
    @SerializedName("createdAt")
    val createdAt: Date
) {

    data class User(
        @Expose
        @SerializedName("id")
        val id: String,

        @Expose
        @SerializedName("name")
        val name: String,

        @Expose
        @SerializedName("profilePicUrl")
        val profilePicUrl: String?
    )
}