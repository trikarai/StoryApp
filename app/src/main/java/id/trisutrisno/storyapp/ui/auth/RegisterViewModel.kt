package id.trisutrisno.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.trisutrisno.storyapp.data.remote.user.register.RegisterRequest
import id.trisutrisno.storyapp.data.remote.user.register.RegisterResponse
import id.trisutrisno.storyapp.repository.UserRepository
import kotlinx.coroutines.launch
import id.trisutrisno.storyapp.utils.Result

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private var _registerResponse  = MutableLiveData<Result<RegisterResponse>>()
    val registerResponse : LiveData<Result<RegisterResponse>> = _registerResponse

    fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _registerResponse.value = Result.Loading()
        try {
            val response = repository.register(registerRequest)
            _registerResponse.value = Result.Success(response.body()!!)
        } catch (e: Exception) {
            _registerResponse.value = Result.Error(e.message)
        }
    }
}