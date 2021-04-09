package com.example.steamnewsrssviewer.newsdata

data class SteamNews(
    val appnews: Appnews
)

data class Appnews(
    val appid: Int,
    val count: Int,
    val newsitems: List<Newsitem>
)

data class Newsitem(
    val appid: Int,
    val author: String,
    val contents: String,
    val date: Int,
    val feed_type: Int,
    val feedlabel: String,
    val feedname: String,
    val gid: String,
    val is_external_url: Boolean,
    val title: String,
    val url: String
)