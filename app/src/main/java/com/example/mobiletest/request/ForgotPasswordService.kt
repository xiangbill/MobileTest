package com.example.mobiletest.request

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 忘记密码 API 服务接口
 */
interface ForgotPasswordService {
    @FormUrlEncoded
    @POST("user/forgot-password")
    fun resetPassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Observable<ResponseBody>

    /**
     * 协程版本的重置密码接口
     */
    @FormUrlEncoded
    @POST("user/forgot-password")
    suspend fun resetPasswordSuspend(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): ResponseBody
}
