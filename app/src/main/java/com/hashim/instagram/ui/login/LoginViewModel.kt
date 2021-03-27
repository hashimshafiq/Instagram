package com.hashim.instagram.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.*
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository

) : BaseViewModel(
    schedulerProvider, compositeDisposable, networkHelper
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
                loggingIn.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserLogin(email,password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            userRepository.saveCurrentUser(user = it)
                            loggingIn.postValue(false)
                            launchMain.postValue(Event(emptyMap()))
                        },{
                            loggingIn.postValue(false)
                            handleNetworkError(it)
                        })
                )
            }

        }
    }

}