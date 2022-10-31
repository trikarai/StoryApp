package id.trisutrisno.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.trisutrisno.storyapp.data.remote.response.StoryResponse
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch
import id.trisutrisno.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun fetchAllStoryWithLocation(token: String): LiveData<Result<List<Story>>> =
        storyRepository.fetchAllStoryWithLocation(token)
}