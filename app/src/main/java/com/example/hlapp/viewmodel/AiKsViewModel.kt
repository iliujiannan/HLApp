package com.example.hlapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hlapp.api.DetectResponse
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

    val detectResult = MutableLiveData<DetectResponse>()

    fun doDetect(filePath: String) {
        if (ServiceCreator.jwt.isEmpty()) {
            getUserKeyAndLogin(filePath)
        } else {
            detect(filePath)
        }
    }

    private fun getUserKeyAndLogin(filePath: String) {
        ServiceCreator.create(HLApi::class.java).getUserKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ServiceCreator.userKey = it.data.userKey
                    ToastUtil.toast("user key: ${ServiceCreator.userKey}")
                    doLogin(filePath)
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("get user key error")
                })
                .autoDispose()
    }

    private fun doLogin(filePath: String) {
        ServiceCreator.create(HLApi::class.java).login(model = LoginRequestModel(userKey = ServiceCreator.userKey))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.toast("login success and jwt = ${it.data.jwt}")
                    ServiceCreator.jwt = it.data.jwt
                    detect(filePath)
                }, {
                    it.printStackTrace()
                    ToastUtil.toast("login error and user key = ${ServiceCreator.userKey}")
                })
                .autoDispose()
    }

    private fun detect(filePath: String) {
        val file = File(filePath)
        val body = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        ServiceCreator.create(HLApi::class.java).doRecognize(file = part, authorization = ServiceCreator.jwt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ToastUtil.toast("detect success and result: ${it.data.recommend}")
                    detectResult.value = it.data
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