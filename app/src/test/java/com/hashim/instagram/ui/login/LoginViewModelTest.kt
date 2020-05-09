package com.hashim.instagram.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.utils.TestSchedulerProvider
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var loggingInObserver : Observer<Boolean>

    @Mock
    private lateinit var launchMainObserver: Observer<Event<Map<String,String>>>

    @Mock
    private lateinit var messagingStringIdObserver : Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider =
            TestSchedulerProvider(testScheduler)

        loginViewModel = LoginViewModel(
            testSchedulerProvider,
            compositeDisposable,
            networkHelper,
            userRepository
        )

        loginViewModel.loggingIn.observeForever(loggingInObserver)
        loginViewModel.launchMain.observeForever(launchMainObserver)
        loginViewModel.messageStringId.observeForever(messagingStringIdObserver)
    }

    @Test
    fun givenServerResponse200_whenLogin_shouldLaunchMainActivity(){

        val email = "test@gmail.com"
        val password = "password"
        val user = User("id","test",email,"accessToken")
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(Single.just(user))
            .`when`(userRepository)
            .doUserLogin(email,password)

        loginViewModel.doLogin()
        testScheduler.triggerActions()
        verify(userRepository).saveCurrentUser(user)
        assert(loginViewModel.loggingIn.value==false)
        assert(loginViewModel.launchMain.value == Event(hashMapOf<String,String>()))
        verify(loggingInObserver).onChanged(true)
        verify(loggingInObserver).onChanged(false)
        verify(launchMainObserver).onChanged(Event(hashMapOf()))
    }

    @Test
    fun givenNoInternet_whenLogin_shouldShowNetworkError(){
        val email = "test@gmail.com"
        val password = "password"
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        loginViewModel.doLogin()

        assert(loginViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        verify(messagingStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
    }

    @After
    fun tearDown(){
        loginViewModel.loggingIn.removeObserver(loggingInObserver)
        loginViewModel.launchMain.removeObserver(launchMainObserver)
        loginViewModel.messageStringId.removeObserver(messagingStringIdObserver)
    }

}