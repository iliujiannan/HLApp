package com.example.hlapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hlapp.api.HLApi
import com.example.hlapp.util.ToastUtil
import com.example.liveandroidpractice.model.data.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by liujiannan on 2020/12/10
 */
class AiKsViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private var userKey: String = ""

    fun getUserKeyAndLogin() {
        ServiceCreator.create(HLApi::class.java).getUserKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    userKey = it.data.userKey
                    ToastUtil.toast("user key: $userKey")
                    doLogin()
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("get user key error")
                })
                .autoDispose()
    }

    private fun doLogin() {
        ServiceCreator.create(HLApi::class.java).login(userKey = userKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    ToastUtil.toast("login success and user key = $userKey" )
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("login error and user key = $userKey")
                })
                .autoDispose()
    }

    private fun Disposable.autoDispose() = compositeDisposable.add(this)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}