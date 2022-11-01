package id.trisutrisno.storyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.trisutrisno.storyapp.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {

//    fun fetchUser(): LiveData<String> {
//        return repository.getUser()
//    }

}