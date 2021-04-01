package com.example.steamnewsrssviewer.retrofitservice

import com.example.steamnewsrssviewer.newsdata.SteamNews
import retrofit2.Call
import retrofit2.http.GET

interface SteamNewsService {
    @GET("?appid=739630&maxlength=300&format=json")
    fun getNews(): Call<SteamNews>
}