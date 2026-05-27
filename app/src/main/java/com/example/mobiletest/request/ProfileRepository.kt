package com.example.mobiletest.request

import com.example.mobiletest.model.ApiResult
import com.example.mobiletest.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 已切换为真实请求架构的 Repository
 */
class ProfileRepository {

    // 拿到定义的 Service 实例
    private val apiService = NetworkModule.profileService

    /**
     * 这里的 Flow 封装了从加载到成功/失败的完整链路。
     */
    fun fetchUserProfile(): Flow<ApiResult<UserProfile>> = flow {
        // 1. 通知 UI 开始加载
        emit(ApiResult.Loading)
        
        try {
            /**
             * 【已切换为真实请求】：
             * 调用 Retrofit 定义的 suspend 函数
             */
            val response = apiService.getUserProfile() 
            
            // 将真实结果发给 UI
            emit(ApiResult.Success(response)) 

        } catch (e: Exception) {
            /**
             * 2. 处理真实网络异常（如断网、404、500、解析错误等）
             */
            emit(ApiResult.Error("请求失败: ${e.localizedMessage}"))
            
            // --- 降级处理：如果真实请求失败，为了演示效果，你可以选择在此处 emit mock 数据 ---
            /*
            delay(1000)
            val fallbackUser = UserProfile("演示账号", "test@example.com", "Free", 0)
            emit(ApiResult.Success(fallbackUser))
            */
        }
    }.flowOn(Dispatchers.IO) 

    /**
     * 持续数据订阅流（保留原样）
     */
    fun getPointsFlow(): Flow<Int> = flow {
        var points = 1000
        while (true) {
            delay(3000)
            points += (2..8).random()
            emit(points)
        }
    }.flowOn(Dispatchers.Default)
}
