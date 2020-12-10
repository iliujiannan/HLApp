package com.example.hlapp.util

import android.widget.Toast

/**
 * Created by liujiannan on 2020/12/10
 */
object ToastUtil {
    fun toast(resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    fun toast(str: CharSequence) = Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}