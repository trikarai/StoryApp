package id.trisutrisno.storyapp.data.remote.response

import com.squareup.moshi.Json
import id.trisutrisno.storyapp.data.remote.dto.StoryDto

data class StoryResponse(
    @Json(name = "listStory")
    val listStory: List<StoryDto>,

    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String
)

