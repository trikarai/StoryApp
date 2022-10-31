package id.trisutrisno.storyapp.domain.model

import id.trisutrisno.storyapp.data.remote.dto.LoginResultDto

data class LoginResult(
    val name: String, val userId: String, val token: String
)

fun LoginResultDto.toDomain(): LoginResult {
    return LoginResult(
        name = name.orEmpty(),
        userId = userId.orEmpty(),
        token = token.orEmpty(),
    )
}

fun LoginResult.toLoggedInUser(): User {
    return User(
        userId = userId,
        name = name,
        token = token,
        isLogin = true
    )
}