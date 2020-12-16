package com.example.hlapp.api

import com.example.hlapp.util.getTimestampString
import com.example.hlapp.model.Response
import com.example.hlapp.util.getDateTimeString
import com.example.liveandroidpractice.model.data.ServiceCreator
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.File
import java.util.*

/**
 * Created by liujiannan on 2020/11/10
 */
interface HLApi {
    @GET("/account/login")
    fun getUserKey(@Query("tokenKey") tokenKey: String = "get"): Observable<Response<UserKey>>

    @Headers("Content-Type: application/json")
    @POST("/account/login")
    fun login(
            @Body model: LoginRequestModel
    ): Observable<Response<LoginResponseModel>>

    @Multipart
    @POST("/pro/detect/v101?methodName=detect")
    fun doRecognize(
            @Part file: MultipartBody.Part,
            @Query("") methodName: String = "",
            @Query("") timestamp: Date = getDateTimeString(),
            @Header("authorization") authorization: String,
            @Header("appId") appId: String = ServiceCreator.APP_ID
    ): Observable<Response<DetectResponse>>

    @Multipart
    @POST("/demo/getAIKanSheRes")
    fun getAiKsRes(@Part file: MultipartBody.Part): Observable<Any>
}

data class UserKey(
        @SerializedName("userKey") val userKey: String = ""
)

data class LoginRequestModel(
        @SerializedName("appId") val appId: String = ServiceCreator.APP_ID,
        @SerializedName("methodName") val method: String = "login",
        @SerializedName("password") val psw: String = ServiceCreator.PSW,
        @SerializedName("timestamp") val timestamp: String = getTimestampString(),
        @SerializedName("userKey") val userKey: String
)

data class LoginResponseModel(
        @SerializedName("jwt") val jwt: String
)

data class DetectResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("feel") val feel: String = "",
        @SerializedName("recomand") val recommend: String = "",
        @SerializedName("typeName") val typeName: String = "",
        @SerializedName("shemianName") val schemianName: String = "",
        @SerializedName("shemianConfidence") val shemianConfidence: String = ""
)
