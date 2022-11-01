package id.trisutrisno.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.trisutrisno.storyapp.data.local.datastore.UserPreference
import id.trisutrisno.storyapp.data.remote.api.ApiService
import id.trisutrisno.storyapp.data.remote.request.LoginRequest
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.domain.model.LoginResult
import id.trisutrisno.storyapp.domain.model.User
import id.trisutrisno.storyapp.domain.model.toDomain
import id.trisutrisno.storyapp.utils.Result

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {
    fun login (email: String, password: String)  : LiveData<Result<LoginResult>> =
    liveData {
        emit(Result.Loading())
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {
                emit(Result.Success(response.loginResult!!.toDomain()))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message))
        }
    }

    fun register(name: String, email: String, password: String): LiveData<Result<GeneralResponse>> =
        liveData {
            emit(Result.Loading())
            try {
                val response = apiService.register(name, email, password)

                if (response.error == false) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
        }

    suspend fun saveUser(user: User) = userPreference.saveUser(user)

    fun getUser() = userPreference.getUser()

    suspend fun deleteUser() = userPreference.deleteUser()

//    fun fetchUser() = userPreference.fetchUser()

    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                UserRepository(apiService, userPreference).also {
                    INSTANCE = it
                }
            }
        }
    }
}