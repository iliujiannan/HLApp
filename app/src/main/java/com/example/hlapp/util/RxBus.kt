package com.example.hlapp.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by liujiannan on 2020/11/10
 * 基于RxJava的事件总线，没有处理背压和sticky
 */
object RxBus {

    private val mBus = PublishSubject.create<Any>().toSerialized()

    fun post(event: Any) {
        mBus.onNext(event)
    }

    fun <T> register(eventType: Class<T>?): Observable<T>? {
        return mBus.ofType(eventType)
                .observeOn(AndroidSchedulers.mainThread())
    }
}