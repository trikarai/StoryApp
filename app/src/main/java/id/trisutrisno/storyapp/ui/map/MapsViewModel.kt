package id.trisutrisno.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.trisutrisno.storyapp.data.remote.stories.StoryResponse
import id.trisutrisno.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch
import id.trisutrisno.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _storyResponse = MutableLiveData<Result<StoryResponse>>()
    val storyResponse: LiveData<Result<StoryResponse>> = _storyResponse

    fun fetchAllStoryWithLocation(token: String) = viewModelScope.launch {
        _storyResponse.value = Result.Loading()
        try {
            val response = storyRepository.fetchAllStoryWithLocation(token)
            if (response.isSuccessful){
                _storyResponse.value = Result.Success(response.body()!!)
            }
        } catch (e: Exception) {
            _storyResponse.value = Result.Error(e.message)
        }
    }

}