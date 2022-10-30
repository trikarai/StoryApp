package id.trisutrisno.storyapp.ui.upload

import androidx.lifecycle.*
import id.trisutrisno.storyapp.data.remote.stories.UploadResponse
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import id.trisutrisno.storyapp.utils.Result
import kotlinx.coroutines.launch


class UploadStoryViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
    ) : ViewModel() {

    private val _uploadResponse = MutableLiveData<Result<UploadResponse>>()
    val uploadResponse: LiveData<Result<UploadResponse>> = _uploadResponse


    fun uploadStory(token: String,
                    file: MultipartBody.Part,
                    description: RequestBody,
                    lat: RequestBody?,
                    lon: RequestBody?) = viewModelScope.launch {
        _uploadResponse.value = Result.Loading()
        try {
            val response = storyRepository.uploadStory(token, file, description, lat, lon)
            _uploadResponse.value = Result.Success(response.body()!!)
        } catch (e: Exception) {
            _uploadResponse.value = Result.Error(e.message.toString())
        }
    }

    fun fetchUser(): LiveData<String> {
        return userRepository.fetchUser().asLiveData()
    }

}