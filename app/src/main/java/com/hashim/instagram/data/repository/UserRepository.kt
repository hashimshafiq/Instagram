package com.hashim.instagram.data.repository

import com.hashim.instagram.data.local.db.DatabaseService
import com.hashim.instagram.data.local.prefs.UserPreferences
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.remote.NetworkService
import com.hashim.instagram.data.remote.request.LoginRequest
import com.hashim.instagram.data.remote.request.ProfileUpdateRequest
import com.hashim.instagram.data.remote.request.SignupRequest
import com.hashim.instagram.data.remote.response.GeneralResponse
import com.hashim.instagram.data.remote.response.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val userPreferences: UserPreferences
) {

    fun saveCurrentUser(user: User) {
        userPreferences.setUserId(user.id)
        userPreferences.setUserName(user.name)
        userPreferences.setUserEmail(user.email)
        userPreferences.setAccessToken(user.accessToken)
    }

    fun removeCurrentUser() {
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
    }

    fun getCurrentUser(): User? {

        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val accessToken = userPreferences.getAccessToken()

        return if (userId !== null && userName != null && userEmail != null && accessToken != null)
            User(userId, userName, userEmail, accessToken)
        else
            null
    }

    fun getThemeMode() : String?{
        return userPreferences.getThemeMode()
    }

    fun saveThemeMode(mode : String){
        userPreferences.setThemeMode(mode)
    }

    fun isThemeChange() : Boolean{
        return userPreferences.isThemeChange()
    }

    fun saveThemeChange(isChange : Boolean){
        userPreferences.setThemeChange(isChange)
    }



    fun doUserLogin(email : String, password : String) : Flow<User> {

        return flow {
            val response = networkService.doLoginCall(LoginRequest(email,password))
            emit(User(
                response.userId,
                response.userName,
                response.userEmail,
                response.accessToken,
                response.profilePicUrl
            ))
        }.flowOn(Dispatchers.IO)
    }



    fun doUserSignup(name : String, email : String, password : String) : Flow<User> {

        return flow {
            val response = networkService.doSignupCall(SignupRequest(email,password,name))
            emit(
                User(
                    response.userId,
                    response.userName,
                    response.userEmail,
                    response.accessToken,
                    response.profilePicUrl
                )
            )
        }.flowOn(Dispatchers.IO)
    }


    fun doUserProfileFetch(user: User) : Flow<ProfileResponse> {
        return flow {
            val response = networkService.doProfileGetCall(user.id,user.accessToken)
            emit(response)
        }.flowOn(Dispatchers.IO)

    }

    fun doLogout(user: User) : Flow<GeneralResponse> {
        return flow {
            val response = networkService.doLogoutCall(user.id, user.accessToken)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    fun doUserProfileUpdate(user: User,name : String?, tagline : String?, profilePicUrl : String?) : Flow<ProfileResponse.User> {
        return flow {
            networkService.doProfileUpdateCall(ProfileUpdateRequest(name,profilePicUrl,tagline), user.id,user.accessToken)
            emit(
                ProfileResponse.User(user.id,name,profilePicUrl,tagline)
            )
        }.flowOn(Dispatchers.IO)
    }




}