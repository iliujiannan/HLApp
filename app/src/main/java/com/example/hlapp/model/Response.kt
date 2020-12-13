package com.example.hlapp.model

import com.google.gson.annotations.SerializedName

data class Response<T>(
        @SerializedName("data") val data: T,
        @SerializedName("meta") val metaData: MetaData
)

data class MetaData(
        @SerializedName("msg") val msg: String,
        @SerializedName("code") val code: Int,
        @SerializedName("success") val success: Boolean,
        @SerializedName("timestamp") val timestamp: String
)