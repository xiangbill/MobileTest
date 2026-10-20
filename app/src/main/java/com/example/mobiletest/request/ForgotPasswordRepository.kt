package com.example.mobiletest.request

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody

class ForgotPasswordRepository {
    private val apiService = NetworkModule.forgotPasswordService

    fun resetPassword(oldPassword: String, newPassword: String): Observable<ResponseBody> {
        return apiService.resetPassword(oldPassword, newPassword)
    }

    /**
     * 协程版本的 Repository 方法
     */
    suspend fun resetPasswordSuspend(oldPassword: String, newPassword: String): ResponseBody {
        return apiService.resetPasswordSuspend(oldPassword, newPassword)
    }
}
