package com.hashim.instagram.data.repository

import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor (private val networkService: NetworkService) {

     fun uploadPhoto(file: File, user: User): Flow<String> {

            return flow {

                val multipart = MultipartBody.Part.createFormData(
                    "image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))

                val api = networkService.doImageUpload(multipart, user.id, user.accessToken)
                emit(api.data.imageUrl)
            }.flowOn(Dispatchers.IO)
    }
}