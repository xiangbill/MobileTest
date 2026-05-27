package com.example.mobiletest.model

data class UserProfile(
    val name: String,
    val email: String,
    val level: String,
    val securityScore: Int
)

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}
