package com.example.hlapp.util

import android.content.Context

fun dp2Px(context: Context, dpValue: Int): Float = dpValue * context.resources.displayMetrics.density + 0.5f

fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

fun getScreenHeight(context: Context) = context.resources.displayMetrics.heightPixels

fun getCacheRootPath(context: Context) = context.externalCacheDir?.path

lateinit var context: Context