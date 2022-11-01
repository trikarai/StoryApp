package id.trisutrisno.storyapp.utils

import id.trisutrisno.storyapp.data.remote.dto.LoginResultDto
import id.trisutrisno.storyapp.data.remote.dto.StoryDto
import id.trisutrisno.storyapp.data.remote.response.GeneralResponse
import id.trisutrisno.storyapp.data.remote.response.LoginResponse
import id.trisutrisno.storyapp.data.remote.response.StoryResponse
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResultDto = LoginResultDto(
            name = "Tri",
            userId = "user",
            token = "token"
        )

        return LoginResponse(
            loginResult = loginResultDto, error = false, message = "success"
        )
    }

    fun generateDummyGeneralResponse(): GeneralResponse {
        return GeneralResponse(error = false, message = "success")
    }

    fun generateDummyStoriesWithoutLocation(): List<Story> {
        val stories = arrayListOf<Story>()
        for (i in 1..10) {
            val story = Story(
                id = "story",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-dasdasdaa5_dummy-pic.png",
                createdAt = "2022-01-01T01:00:00.598Z",
                name = "Tri",
                description = "Lorem Ipsum",
                lon = null,
                lat = null
            )
            stories.add(story)
        }
        return stories
    }

    fun generateDummyStoriesWithLocation(): List<Story> {
        val stories = arrayListOf<Story>()
        for (i in 1..10) {
            val story = Story(
                id = "story",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-01T06:34:18.598Z",
                name = "Tri",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            stories.add(story)
        }
        return stories
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateDummyLoggedInUser(): User {
        return User(
            userId = "user",
            name = "Tri",
            token = "token",
            isLogin = true
        )
    }

    fun generateDummyLoggedOutUser(): User {
        return User(
            "", "", "", false
        )
    }

    fun generateDummyStoriesResponse(): StoryResponse {
        val stories = mutableListOf<StoryDto>()
        for (i in 1..10) {
            val storyEntity = StoryDto(
                id = "story",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-164162365444_dummy-pic.png",
                createdAt = "2022-01-01T06:34:18.598Z",
                name = "Tri",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            stories.add(storyEntity)
        }
        return StoryResponse(
            listStory = stories, error = false, message = "success"
        )
    }

    fun generateDummyEmptyStoriesResponse(): StoryResponse {
        val stories = mutableListOf<StoryDto>()
        return StoryResponse(
            listStory = stories, error = false, message = "success"
        )
    }

    fun generateDummyRegisterResponse(isSuccess: Boolean): GeneralResponse {
        return if (isSuccess) {
            GeneralResponse(error = false, message = "success")
        } else {
            GeneralResponse(error = true, message = "Email already taken")
        }
    }

    fun generateDummyLoginResponses(isSuccess: Boolean): LoginResponse {
        return if (isSuccess) {
            val loginResultDto = LoginResultDto(
                name = "Tri",
                userId = "user",
                token = "token"
            )

            LoginResponse(error = false, message = "success", loginResult = loginResultDto)
        } else {
            LoginResponse(error = true, message = "Email or Password is incorrect")
        }
    }

    fun generateDummyPostStoryResponse(): GeneralResponse {
        return GeneralResponse(error = false, message = "success")
    }


}