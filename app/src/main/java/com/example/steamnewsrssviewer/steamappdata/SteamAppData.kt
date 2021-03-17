package com.example.steamnewsrssviewer.steamappdata

import com.google.gson.annotations.SerializedName

data class SteamAppData(
    val applist: Applist
)

data class Applist(
    val apps: List<App>
)

data class App(
    val appid: Int,
    val name: String
)