package com.example.hlapp.base

import android.app.Application
import com.example.hlapp.util.context

/**
 * Created by liujiannan on 2020/12/10
 */
class HlApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}