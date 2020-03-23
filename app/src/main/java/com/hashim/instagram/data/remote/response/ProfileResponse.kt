package com.hashim.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("status")
    var status: Int,

    @Expose
    @SerializedName("message")
    var message: String,

    @Expose
    @SerializedName("data")
    var data: User?

){
    data class User(
        @Expose
        @SerializedName("id")
        val id: String?,

        @Expose
        @SerializedName("name")
        val name: String?,

        @Expose
        @SerializedName("profilePicUrl")
        val profilePicUrl: String?,

        @Expose
        @SerializedName("tagline")
        val tagLine: String?
    )

}
