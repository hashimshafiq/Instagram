package com.hashim.instagram.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.*
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository

) : BaseViewModel(
    coroutineDispatchers, networkHelper
){


    override fun onCreate() {}

    private val validationList : MutableLiveData<List<Validation>> = MutableLiveData()
    val emailField : MutableLiveData<String> = MutableLiveData()
    val passwordField : MutableLiveData<String> = MutableLiveData()
    val loggingIn : MutableLiveData<Boolean> = MutableLiveData()
    val emailValidation : LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation : LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()



    private fun filterValidation(field : Validation.Field) =
        Transformations.map(validationList){ it ->
            it.find { it.field == field }
                ?.run { return@run this.resource }
                ?:Resource.unknown()
        }

    fun onEmailChange(email : String) = emailField.postValue(email)

    fun onPasswordChange(password : String) = passwordField.postValue(password)

    fun doLogin(){
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateLoginFields(email,password)
        validationList.postValue(validations)
        if(validations.isNotEmpty() && email !=null && password != null){
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if(successValidation.size == validations.size && checkInternetConnectionWithMessage()){

                viewModelScope.launch(coroutineDispatchers.io()) {

                    try {
                        userRepository.doUserLogin(email, password)
                            .onStart { loggingIn.postValue(true) }
                            .collect {
                                userRepository.saveCurrentUser(user = it)
                                loggingIn.postValue(false)
                                launchMain.postValue(Event(emptyMap()))
                            }
                    }catch (ex: Exception){
                        loggingIn.postValue(false)
                        handleNetworkError(ex)
                    }
                }

            }

        }
    }

}