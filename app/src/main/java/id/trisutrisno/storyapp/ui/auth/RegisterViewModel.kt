package id.trisutrisno.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.repository.UserRepository
import id.trisutrisno.storyapp.utils.Result

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<Result<GeneralResponse>> =
        repository.register(name, email, password)
}