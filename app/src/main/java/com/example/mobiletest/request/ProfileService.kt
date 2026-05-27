package com.example.mobiletest.request

import com.example.mobiletest.model.UserProfile
import retrofit2.http.GET

/**
 * Retrofit 接口定义
 */
interface ProfileService {
    @GET("user/profile") // 这里的路径可以根据你的真实接口修改
    suspend fun getUserProfile(): UserProfile
}
