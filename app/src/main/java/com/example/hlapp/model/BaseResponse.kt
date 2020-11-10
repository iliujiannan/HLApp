package com.example.liveandroidpractice.model.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
        @SerializedName("data") var data: T?,
        @SerializedName("errorCode") var errorCode: Int,
        @SerializedName("errorMsg") var errorMsg: String?
)