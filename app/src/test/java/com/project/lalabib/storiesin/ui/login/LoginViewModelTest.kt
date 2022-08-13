package com.project.lalabib.storiesin.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.data.Result
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.response.LoginResponse
import com.project.lalabib.storiesin.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResponse()

    private val email = "email@gmail.com"
    private val password = "password"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Login is Success`() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Success(dummyLogin)
        `when`(loginViewModel.postLogin(email, password)).thenReturn(expectedUser)

        val actualUser = loginViewModel.postLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).postLogin(email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
    }
}