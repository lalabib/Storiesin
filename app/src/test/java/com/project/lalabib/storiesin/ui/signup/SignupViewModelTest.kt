package com.project.lalabib.storiesin.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.response.RegisterResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.project.lalabib.storiesin.data.Result
import com.project.lalabib.storiesin.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: SignupViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()

    private val name = "Your Name"
    private val email = "email@gmail.com"
    private val password = "password"

    @Before
    fun setup() {
        registerViewModel = SignupViewModel(storyRepository)
    }

    @Test
    fun `when Registration is success`() {
        val expectedUser = MutableLiveData<Result<RegisterResponse>>()
        expectedUser.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(registerViewModel.postRegister(name, email, password)).thenReturn(expectedUser)

        val actualUser = registerViewModel.postRegister(name, email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).postRegister(name, email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
    }
}