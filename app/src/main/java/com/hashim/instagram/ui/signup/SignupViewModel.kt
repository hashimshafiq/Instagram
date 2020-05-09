package com.hashim.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.common.*
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignupViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    val userRepository: UserRepository
    ) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val validationList : MutableLiveData<List<Validation>> = MutableLiveData()
    val emailField : MutableLiveData<String> = MutableLiveData()
    val passwordField : MutableLiveData<String> = MutableLiveData()
    val fullNameField : MutableLiveData<String> = MutableLiveData()
    val siginningUp : MutableLiveData<Boolean> = MutableLiveData()

    val emailValidation : LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation : LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)
    val nameValidation : LiveData<Resource<Int>> = filterValidation(Validation.Field.TEXTFIELD)
    val launchMain : MutableLiveData<Event<Map<String,String>>> = MutableLiveData()

    fun onEmailChange(email : String) = emailField.postValue(email)

    fun onPasswordChange(password : String) = passwordField.postValue(password)

    fun onNameChange(name : String) = fullNameField.postValue(name)

    private fun filterValidation(field : Validation.Field) =
        Transformations.map(validationList){it ->
            it.find {it.field == field }
                ?.run { return@run this.resource }
                ?:Resource.unknown()
        }


    override fun onCreate() {}

    fun doSignup(){
        val name = fullNameField.value
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateSignupFields(name, email, password)
        validationList.postValue(validations)
        if(validations.isNotEmpty() && name != null && email != null && password != null){
            val successValidation = validations.filter { it.resource.status==Status.SUCCESS }
            if(successValidation.size==validations.size && checkInternetConnectionWithMessage()){
                siginningUp.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserSignup(name, email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            userRepository.saveCurrentUser(it)
                            siginningUp.postValue(false)
                            launchMain.postValue(Event(emptyMap()))

                        },{
                            siginningUp.postValue(false)
                            handleNetworkError(it)
                        }
                    )
                )

            }
        }
    }


}