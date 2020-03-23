package com.hashim.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest (

    @Expose
    @SerializedName("email")
    private val email : String,

    @Expose
    @SerializedName("password")
    private val password : String

)