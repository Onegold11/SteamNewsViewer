package com.example.steamnewsrssviewer

import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface SteamNewsService {
    @GET("/feeds/news/app/739630")
    fun getNews(): Call<Rss>
}