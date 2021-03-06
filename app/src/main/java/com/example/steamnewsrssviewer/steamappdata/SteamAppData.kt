package com.example.steamnewsrssviewer.steamappdata

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