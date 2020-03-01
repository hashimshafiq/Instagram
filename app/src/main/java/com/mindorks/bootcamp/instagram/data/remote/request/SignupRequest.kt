package com.mindorks.bootcamp.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignupRequest (

    @Expose
    @SerializedName("email")
    private val email : String,

    @Expose
    @SerializedName("password")
    private val password : String,

    @Expose
    @SerializedName("name")
    private val name : String



)