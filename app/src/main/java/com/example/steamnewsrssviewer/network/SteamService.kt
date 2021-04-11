package com.example.steamnewsrssviewer.network

import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.steamappdata.SteamAppDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamService {
    @GET("api/appdetails?")
    fun getSteamAppDetail(@Query("appids") appId: String): Call<Map<String, SteamAppDetailData>>

    @GET("ISteamApps/GetAppList/v2/?")
    fun getSteamAppList(): Call<SteamAppData>

    @GET("ISteamNews/GetNewsForApp/v0002/?")
    fun getNews(@Query("appid") appId: String): Call<SteamNews>
}