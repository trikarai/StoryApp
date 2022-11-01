package id.trisutrisno.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun fetchAllStoryWithLocation(token: String): LiveData<Result<List<Story>>> =
        storyRepository.fetchAllStoryWithLocation(token)
}