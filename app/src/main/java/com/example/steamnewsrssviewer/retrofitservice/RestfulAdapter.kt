package com.example.steamnewsrssviewer.retrofitservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestfulAdapter {
    private const val STEAM_APP_LIST_URL = "https://api.steampowered.com/ISteamApps/GetAppList/v2/"
    private const val STEAM_DETAIL_URL = "https://store.steampowered.com/api/"
    private const val STEAM_NEWS_URL = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/"

    fun getSteamAppListApi(): SteamAppService{
        val retrofit = Retrofit.Builder()
            .baseUrl(STEAM_APP_LIST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SteamAppService::class.java)
    }

    fun getSteamAppDetailApi(): SteamAppDetailService{
        val retrofit = Retrofit.Builder()
            .baseUrl(STEAM_DETAIL_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SteamAppDetailService::class.java)
    }

    fun getSteamNewsApi(): SteamNewsService{
        val retrofit = Retrofit.Builder()
            .baseUrl(STEAM_NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SteamNewsService::class.java)
    }
}