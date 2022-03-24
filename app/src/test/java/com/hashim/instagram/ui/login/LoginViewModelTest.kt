package com.hashim.instagram.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.data.model.User
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.utils.MainCoroutineRule
import com.hashim.instagram.utils.TestCoroutineDispatcherProvider
import com.hashim.instagram.utils.common.Event
import com.hashim.instagram.utils.common.Resource
import com.hashim.instagram.utils.network.NetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

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

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var testCoroutineDispatchers: TestCoroutineDispatcherProvider

    private lateinit var testCoroutineScheduler: TestCoroutineScheduler

    @Before
    fun setup(){
        testCoroutineScheduler = TestCoroutineScheduler()
        testCoroutineDispatchers = TestCoroutineDispatcherProvider(testCoroutineScheduler)
        loginViewModel = LoginViewModel(testCoroutineDispatchers, networkHelper, userRepository)
        loginViewModel.loggingIn.observeForever(loggingInObserver)
        loginViewModel.launchMain.observeForever(launchMainObserver)
        loginViewModel.messageStringId.observeForever(messagingStringIdObserver)
    }

    @Test
    fun givenServerResponse200_whenLogin_shouldLaunchMainActivity() = runBlocking {

        val email = "test@gmail.com"
        val password = "password"
        val user = User("id","test",email,"accessToken")
        loginViewModel.emailField.value = email
        loginViewModel.passwordField.value = password
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(flow { emit(user) })
            .`when`(userRepository)
            .doUserLogin(email,password)

        loginViewModel.doLogin()

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