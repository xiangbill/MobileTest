package com.example.mobiletest.request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 简单的网络模块，提供 Retrofit 实例
 */
object NetworkModule {
    private const val BASE_URL = "https://api.example.com/" // 替换为你的真实服务器地址

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val profileService: ProfileService by lazy {
        retrofit.create(ProfileService::class.java)
    }
}
