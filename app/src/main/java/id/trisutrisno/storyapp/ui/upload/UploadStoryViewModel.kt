package id.trisutrisno.storyapp.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.repository.StoryRepository
import id.trisutrisno.storyapp.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UploadStoryViewModel(
    private val storyRepository: StoryRepository
    ) : ViewModel() {
    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): LiveData<Result<GeneralResponse>> =
        storyRepository.uploadStory(token, imageMultipart, description, latitude, longitude)
}


