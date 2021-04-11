package com.example.steamnewsrssviewer.steamappdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SteamAppDetailData(
        @SerializedName("success")
        @Expose
        val success: Boolean,

        @SerializedName("data")
        val data: Data
)

data class Data(
        @SerializedName("type")
        @Expose
        val type: String,
)