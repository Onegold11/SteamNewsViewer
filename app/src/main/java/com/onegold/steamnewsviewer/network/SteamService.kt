package com.onegold.steamnewsviewer.network

import com.onegold.steamnewsviewer.newsdata.SteamNews
import com.onegold.steamnewsviewer.steamappdata.SteamAppData
import com.onegold.steamnewsviewer.steamappdata.SteamAppDetailData
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