package com.hashim.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileUpdateRequest (

    @Expose
    @SerializedName("name")
    private val name : String?,

    @Expose
    @SerializedName("profilePicUrl")
    private val profilePicUrl : String?,

    @Expose
    @SerializedName("tagline")
    private val tagline : String?

)