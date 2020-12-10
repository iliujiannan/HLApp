package com.example.hlapp.api

import com.example.hlapp.util.getTimestampString
import com.example.liveandroidpractice.model.data.BaseResponse
import com.example.liveandroidpractice.model.data.ServiceCreator
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by liujiannan on 2020/11/10
 */
interface HLApi {
    @GET("/account/login")
    fun getUserKey(@Query("tokenKey") tokenKey: String = "get"): Observable<BaseResponse<UserKey>>

    @FormUrlEncoded
    @POST("/account/login")
    fun login(
            @Field("appId") appId: String = ServiceCreator.APP_ID,
            @Field("methodName") method: String = "login",
            @Field("password") psw: String = ServiceCreator.PSW,
            @Field("timestamp") timestamp: String = getTimestampString(),
            @Field("userKey") userKey: String
    ): Observable<Any>
}

data class UserKey(
        @SerializedName("userKey") val userKey: String = ""
)