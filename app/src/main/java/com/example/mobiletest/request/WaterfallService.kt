package com.example.mobiletest.request

import com.example.mobiletest.model.GenericItem
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 瀑布流模块的 RxJava 接口定义
 * 包含常用请求类型示例：GET, POST, PUT, DELETE
 */
interface WaterfallService {

    // 1. GET - Path 参数：常用于获取特定 ID 的详情
    @GET("items/{id}")
    fun getItemDetails(@Path("id") id: Int): Observable<GenericItem>

    // 2. GET - Query 参数：常用于筛选、分页 (例如: items?tag=Hot&page=1)
    @GET("items")
    fun getItemsByTag(
        @Query("tag") tag: String,
        @Query("page") page: Int
    ): Observable<List<GenericItem>>

    // 3. POST - Body：提交 JSON 对象
    @POST("items/create")
    fun createItem(@Body item: GenericItem): Observable<GenericItem>

    // 4. POST - FormUrlEncoded：提交表单字段 (键值对)
    @FormUrlEncoded
    @POST("items/submit")
    fun submitItemForm(
        @Field("title") title: String,
        @Field("reason") reason: String
    ): Observable<ResponseBody>

    // 5. PUT - Path + Body：更新特定 ID 的资源
    @PUT("items/{id}")
    fun updateItem(
        @Path("id") id: Int,
        @Body item: GenericItem
    ): Observable<GenericItem>

    // 6. DELETE - Path：删除特定 ID 的资源
    @DELETE("items/{id}")
    fun deleteItem(@Path("id") id: Int): Observable<ResponseBody>

    // 7. Header - 添加自定义请求头
    @GET("items/secret")
    fun getSecretItems(@Header("Authorization") token: String): Observable<List<GenericItem>>
}
