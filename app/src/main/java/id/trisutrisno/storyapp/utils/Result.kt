package id.trisutrisno.storyapp.utils

sealed class Result<T>(val data: T? = null, val message: String? = null)  {
    class Loading<T> : Result<T>()
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String?, data: T? = null) : Result<T>(data, message)
}