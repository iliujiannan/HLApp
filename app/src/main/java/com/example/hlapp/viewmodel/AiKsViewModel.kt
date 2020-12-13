package com.example.hlapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hlapp.api.HLApi
import com.example.hlapp.api.LoginRequestModel
import com.example.hlapp.util.ToastUtil
import com.example.liveandroidpractice.model.data.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * Created by liujiannan on 2020/12/10
 */
class AiKsViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private var userKey: String = ""
    private var jwt: String = ""

    fun doDetect(filePath: String) {
        getUserKeyAndLogin(filePath)
    }

    fun getUserKeyAndLogin(filePath: String) {
        ServiceCreator.create(HLApi::class.java).getUserKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    userKey = it.data.userKey
                    ToastUtil.toast("user key: $userKey")
                    doLogin(filePath)
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("get user key error")
                })
                .autoDispose()
    }

    private fun doLogin(filePath: String) {
        ServiceCreator.create(HLApi::class.java).login(model = LoginRequestModel(userKey = userKey))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.toast("login success and jwt = ${it.data.jwt}")
                    jwt = it.data.jwt
                    detect(filePath)
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("login error and user key = $userKey")
                })
                .autoDispose()
    }

    private fun detect(filePath: String) {
        val file = File(filePath)
        val body = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("picture", file.name, body)
        ServiceCreator.create(HLApi::class.java).doRecognize(file = part, authorization = jwt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.toast("detect success and result: ${it.data}")
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("detect error")
                })
                .autoDispose()
    }

    private fun Disposable.autoDispose() = compositeDisposable.add(this)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}