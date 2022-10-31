package id.trisutrisno.storyapp.data.remote.request

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)