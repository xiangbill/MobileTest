package com.example.mobiletest.request

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 简单的网络模块，提供 Retrofit 实例
 */
object NetworkModule {
    private const val BASE_URL = "https://api.example.com/" // 替换为你的真实服务器地址

    private val okHttpClient: OkHttpClient by lazy {
        // 创建日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY 
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(RetryInterceptor(2)) // 添加自定义重试拦截器，重试 2 次
            .addInterceptor(GlobalErrorInterceptor()) // 添加全局错误拦截器
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 全局 HTTP 错误拦截器 (处理 401, 503 等)
     */
    class GlobalErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            
            when (response.code) {
                401 -> {
                    // 处理登录失效
                    println("GlobalErrorInterceptor: Token Expired (401)")
                }
                503 -> {
                    // 处理服务器维护
                    println("GlobalErrorInterceptor: Server Maintenance (503)")
                }
            }
            return response
        }
    }

    /**
     * 自定义重试拦截器（优化版：区分幂等性）
     */
    class RetryInterceptor(private val maxRetry: Int) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(request)
            var tryCount = 0

            // 只有满足以下条件才进行重试：
            // 1. 请求不成功
            // 2. 未达到最大重试次数
            // 3. 必须是幂等方法 (GET, PUT, DELETE等)，避免 POST 重复提交
            while (!response.isSuccessful && tryCount < maxRetry && isIdempotent(request)) {
                tryCount++
                response.close()
                response = chain.proceed(request)
            }
            return response
        }

        private fun isIdempotent(request: okhttp3.Request): Boolean {
            return when (request.method) {
                "GET", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE" -> true
                else -> false // POST, PATCH 不自动重试，需由业务层处理
            }
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    val profileService: ProfileService by lazy {
        retrofit.create(ProfileService::class.java)
    }

    val waterfallService: WaterfallService by lazy {
        retrofit.create(WaterfallService::class.java)
    }
}
