package id.trisutrisno.storyapp.repository

import id.trisutrisno.storyapp.data.local.datastore.UserPreference
import id.trisutrisno.storyapp.data.remote.ApiService
import id.trisutrisno.storyapp.data.remote.user.login.LoginRequest
import id.trisutrisno.storyapp.data.remote.user.register.RegisterRequest

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {
    suspend fun login (loginRequest: LoginRequest) = apiService.login(loginRequest)
    suspend fun register (registerRequest: RegisterRequest) = apiService.register(registerRequest)

    suspend fun saveUser(token: String) = userPreference.saveUser(token)
    suspend fun deleteUser() = userPreference.deleteUser()

    fun fetchUser() = userPreference.fetchUser()

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