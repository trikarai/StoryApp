package id.trisutrisno.storyapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private  val dataStore: DataStore<Preferences>) {
    suspend fun saveUser(token: String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun deleteUser() {
        dataStore.edit {
            preferences -> preferences.clear()
        }
    }

    fun fetchUser(): Flow<String> {
        return dataStore.data.map { 
            preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
         fun getInstance(dataStore: DataStore<Preferences>) : UserPreference {
             return INSTANCE ?: synchronized(this) {
                 val instance = UserPreference(dataStore)
                 INSTANCE = instance
                 instance
             }
         }
    }
}