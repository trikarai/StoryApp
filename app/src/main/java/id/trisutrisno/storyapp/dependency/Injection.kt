package id.trisutrisno.storyapp.dependency

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import id.trisutrisno.storyapp.data.local.datastore.UserPreference
import id.trisutrisno.storyapp.data.local.room.StoryDatabase
import id.trisutrisno.storyapp.data.remote.ApiConfig
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.repository.UserRepository

object Injection {

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val api = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(dataStore)
        return UserRepository.getInstance(api, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val api = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(api, database)
    }

}