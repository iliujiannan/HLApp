package com.example.hlapp.api

import com.example.liveandroidpractice.model.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by liujiannan on 2020/11/10
 */
interface HLApi {
    @GET("/do_something")
    fun doSomething(): Observable<BaseResponse<Any>>
}