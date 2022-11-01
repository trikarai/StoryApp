package id.trisutrisno.storyapp.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import id.trisutrisno.storyapp.domain.model.LoginResult
import id.trisutrisno.storyapp.domain.model.toDomain
import id.trisutrisno.storyapp.repository.UserRepository
import id.trisutrisno.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import id.trisutrisno.storyapp.utils.Result
import id.trisutrisno.storyapp.utils.getOrAwaitValue

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `Login Success with success result`() {
        val expectedResponse = MutableLiveData<Result<LoginResult>>()
        expectedResponse.value = Result.Success(dummyLoginResponse.loginResult!!.toDomain())

        `when`(userRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse =
            loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success<*>)
        Assert.assertEquals(
            dummyLoginResponse.loginResult!!.toDomain(),
            (actualResponse as Result.Success<*>).data
        )
    }

    @Test
    fun `Login Failed with error result`() {
        val expectedResponse = MutableLiveData<Result<LoginResult>>()
        expectedResponse.value = Result.Error("Error")

        `when`(userRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
        Assert.assertEquals(
            expectedResponse.value?.message,
            actualResponse.message
        )
    }
}