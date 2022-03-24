package com.hashim.instagram.ui.signup

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
import kotlinx.coroutines.test.TestCoroutineScheduler
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
class SignupViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var signUpViewModel: SignupViewModel

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var launchMainObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var messagingStringIdObserver : Observer<Resource<Int>>

    @Mock
    private lateinit var signingUpObserver : Observer<Boolean>

    private lateinit var testCoroutineDispatchers: TestCoroutineDispatcherProvider

    private lateinit var testCoroutineScheduler: TestCoroutineScheduler


    @Before
    fun setup(){

        testCoroutineScheduler = TestCoroutineScheduler()
        testCoroutineDispatchers = TestCoroutineDispatcherProvider(testCoroutineScheduler)

        signUpViewModel = SignupViewModel(testCoroutineDispatchers,networkHelper,userRepository)

        signUpViewModel.siginningUp.observeForever(signingUpObserver)
        signUpViewModel.launchMain.observeForever(launchMainObserver)
        signUpViewModel.messageStringId.observeForever(messagingStringIdObserver)

    }

    @Test
    fun givenServerResponse200_WhenSignup_shouldLaunchMainActivity(){

        val name = "test"
        val email = "test@test.com"
        val password = "testtesttest"

        val user = User("id",name,email,"accessToken","profilePic")

        signUpViewModel.emailField.value = email
        signUpViewModel.passwordField.value = password
        signUpViewModel.fullNameField.value = name

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(flow { emit(user) })
            .`when`(userRepository)
            .doUserSignup(name, email, password)

        signUpViewModel.doSignup()

        verify(userRepository).saveCurrentUser(user)
        assert(signUpViewModel.siginningUp.value==false)
        assert(signUpViewModel.launchMain.value == Event(hashMapOf<String,String>()))
        verify(signingUpObserver).onChanged(true)
        verify(signingUpObserver).onChanged(false)
        verify(launchMainObserver).onChanged(Event(hashMapOf()))

    }

    @Test
    fun givenNoInternet_whenLogin_shouldShowNetworkError(){
        val email = "test@gmail.com"
        val password = "password"
        val name = "test"
        signUpViewModel.emailField.value = email
        signUpViewModel.passwordField.value = password
        signUpViewModel.fullNameField.value = name

        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        signUpViewModel.doSignup()

        assert(signUpViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        verify(messagingStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
    }

    @After
    fun tearDown(){
        signUpViewModel.siginningUp.removeObserver(signingUpObserver)
        signUpViewModel.launchMain.removeObserver(launchMainObserver)
        signUpViewModel.messageStringId.removeObserver(messagingStringIdObserver)
    }


}