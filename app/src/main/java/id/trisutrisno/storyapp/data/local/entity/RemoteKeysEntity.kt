package id.trisutrisno.storyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
