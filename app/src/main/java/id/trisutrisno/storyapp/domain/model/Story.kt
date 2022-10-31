package id.trisutrisno.storyapp.domain.model

import android.os.Parcelable
import id.trisutrisno.storyapp.data.remote.dto.StoryDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double?,
    val id: String,
    val lat: Double?
) : Parcelable

fun StoryDto.toEntity(): Story {
    return Story(
        photoUrl = photoUrl,
        createdAt = createdAt,
        name = name,
        description = description,
        lon = lon,
        id = id,
        lat = lat
    )
}

fun StoryDto.toDomain(): Story {
    return Story(
        photoUrl = photoUrl,
        createdAt = createdAt,
        name = name,
        description = description,
        lon = lon,
        id = id,
        lat = lat
    )
}

fun Story.toDomain(): Story {
    return Story(
        photoUrl = photoUrl,
        createdAt = createdAt,
        name = name,
        description = description,
        lon = lon,
        id = id,
        lat = lat
    )
}

fun Story.toStoryEntity(): Story {
    return Story(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        photoUrl = photoUrl,
        lon = lon,
        lat = lat
    )
}