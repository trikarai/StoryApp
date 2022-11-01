package id.trisutrisno.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import id.trisutrisno.storyapp.data.StoryRemoteMediator
import id.trisutrisno.storyapp.data.local.database.StoryDatabase
import id.trisutrisno.storyapp.data.local.entity.StoryEntity
import id.trisutrisno.storyapp.data.remote.api.ApiService
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.domain.model.toDomain
import okhttp3.MultipartBody
import okhttp3.RequestBody
import id.trisutrisno.storyapp.utils.Result

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase) {

    fun fetchAllStory(token: String): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(generateBearerToken(token), apiService, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().fetchAllStories()
            }).liveData
    }


    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<Result<GeneralResponse>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.uploadImage(
                generateBearerToken(token), file, description, lat, lon
            )

            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }

    fun fetchAllStoryWithLocation(token: String)   : LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading())

        try {
            val response = apiService.fetchStories(
                generateBearerToken(token), size = 10, location = 1
            )

            if (!response.error) {
                emit(Result.Success(response.listStory.map { storyDto ->
                    storyDto.toDomain()
                }))
            } else {
                emit(Result.Error(response.message))
            }


        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }


    companion object {
        private var INSTANCE: StoryRepository? = null
        fun getInstance(apiService: ApiService, storyDatabase: StoryDatabase): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                StoryRepository(apiService, storyDatabase).also {
                    INSTANCE = it
                }
            }
        }
        private fun generateBearerToken(token: String): String {
            return "Bearer $token"
        }
    }

}