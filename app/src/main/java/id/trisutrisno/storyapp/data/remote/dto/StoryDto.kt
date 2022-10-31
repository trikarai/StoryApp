package id.trisutrisno.storyapp.data.remote.dto

import com.squareup.moshi.Json

data class StoryDto(

    @Json(name = "photoUrl")
    val photoUrl: String,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "lon")
    val lon: Double?,

    @Json(name = "id")
    val id: String,

    @Json(name = "lat")
    val lat: Double?
)
