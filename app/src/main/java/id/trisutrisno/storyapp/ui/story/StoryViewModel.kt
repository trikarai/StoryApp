package id.trisutrisno.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.trisutrisno.storyapp.data.local.room.StoryModel
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun fetchAllStory(token: String): LiveData<PagingData<StoryModel>> =
        storyRepository.fetchAllStory(token).cachedIn(viewModelScope)

    fun deleteUser() = viewModelScope.launch {
        userRepository.deleteUser()
    }
}