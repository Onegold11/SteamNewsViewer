package com.example.steamnewsrssviewer.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestfulAdapter {
    private const val STEAM_URL = "https://api.steampowered.com/"

    private fun getSteamRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(STEAM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getSteamApi(): SteamService = getSteamRetrofit().create(SteamService::class.java)

}