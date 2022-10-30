package id.trisutrisno.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.trisutrisno.storyapp.dependency.Injection
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.repository.UserRepository
import id.trisutrisno.storyapp.ui.SplashViewModel
import id.trisutrisno.storyapp.ui.auth.LoginViewModel
import id.trisutrisno.storyapp.ui.auth.RegisterViewModel
import id.trisutrisno.storyapp.ui.story.StoryViewModel
import id.trisutrisno.storyapp.ui.upload.UploadStoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_account")


class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(userRepository, storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        private var INSTANCE: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                UserViewModelFactory(
                    Injection.provideUserRepository(context.dataStore),
                    Injection.provideStoryRepository(context)).also {
                    INSTANCE = it
                }
            }
        }
    }
}