package com.example.steamnewsrssviewer

import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import retrofit2.Call
import retrofit2.http.GET

interface SteamAppService {
    @GET
    fun getSteamApp(): Call<SteamAppData>
}