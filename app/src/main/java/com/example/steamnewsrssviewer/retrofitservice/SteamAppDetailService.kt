package com.example.steamnewsrssviewer.retrofitservice

import com.example.steamnewsrssviewer.steamappdata.SteamAppDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamAppDetailService {
    @GET("appdetails?")
    fun getSteamAppDetail(@Query("appids") appId: String): Call<Map<String, SteamAppDetailData>>
}