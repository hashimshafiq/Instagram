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
import io.reactivex.Single
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



    fun doUserLogin(email : String, password : String) : Single<User> =
        networkService.doLoginCall(LoginRequest(email,password))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }

    fun doUserSignup(name : String, email : String, password : String) : Single<User> =
        networkService.doSignupCall(SignupRequest(email,password,name))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }

    fun doUserProfileFetch(user: User) : Single<ProfileResponse> =
        networkService.doProfileGetCall(user.id,user.accessToken).map {
            return@map it
        }

    fun doLogout(user: User) : Single<GeneralResponse> =
        networkService.doLogoutCall(user.id,user.accessToken).map {
            return@map it
        }

    fun doUserProfileUpdate(user: User,name : String?, tagline : String?, profilePicUrl : String?) : Single<ProfileResponse.User> =
        networkService.doProfileUpdateCall(ProfileUpdateRequest(name,profilePicUrl,tagline), user.id,user.accessToken).map {
            ProfileResponse.User(user.id,name,profilePicUrl,tagline)
        }



}