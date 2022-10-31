package id.trisutrisno.storyapp.data.remote.response

import com.squareup.moshi.Json
import id.trisutrisno.storyapp.data.remote.dto.LoginResultDto

data class LoginResponse(
    @Json(name = "loginResult")
    val loginResult: LoginResultDto? = null,

    @Json(name = "error")
    val error: Boolean? = null,

    @Json(name = "message")
    val message: String? = null
)
