package id.trisutrisno.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.domain.model.toDomain
import id.trisutrisno.storyapp.repository.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun fetchAllStory(token: String): LiveData<PagingData<Story>> =
        storyRepository.fetchAllStory(token).map { result ->
            result.map { storyEntity ->
                storyEntity.toDomain()
            }
        }.cachedIn(viewModelScope)
}