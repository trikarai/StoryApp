package id.trisutrisno.storyapp.data

import id.trisutrisno.storyapp.data.remote.api.ApiService
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.data.remote.response.LoginResponse
import id.trisutrisno.storyapp.data.remote.response.StoryResponse
import id.trisutrisno.storyapp.utils.DataDummy.generateDummyEmptyStoriesResponse
import id.trisutrisno.storyapp.utils.DataDummy.generateDummyLoginResponses
import id.trisutrisno.storyapp.utils.DataDummy.generateDummyPostStoryResponse
import id.trisutrisno.storyapp.utils.DataDummy.generateDummyRegisterResponse
import id.trisutrisno.storyapp.utils.DataDummy.generateDummyStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryService : ApiService {
    override suspend fun register(
        name: String, email: String, password: String
    ): GeneralResponse {
        return if (email == "tri@gmail.com") {
            generateDummyRegisterResponse(true)
        } else {
            generateDummyRegisterResponse(false)
        }
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return if (email == "tri@gmail.com") {
            generateDummyLoginResponses(true)
        } else {
            generateDummyLoginResponses(false)
        }
    }

    override suspend fun uploadImage(
        auth: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): GeneralResponse {
        return generateDummyPostStoryResponse()
    }

    override suspend fun fetchStories(
        auth: String, page: Int?, size: Int?, location: Int?
    ): StoryResponse {
        return if (auth == "Bearer token") {
            generateDummyStoriesResponse()
        } else {
            generateDummyEmptyStoriesResponse()
        }
    }
}