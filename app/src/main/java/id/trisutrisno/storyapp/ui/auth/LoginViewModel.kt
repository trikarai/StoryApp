package id.trisutrisno.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.trisutrisno.storyapp.domain.model.LoginResult
import id.trisutrisno.storyapp.repository.UserRepository
import id.trisutrisno.storyapp.utils.Result

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResult>> =
        repository.login(email, password)
}