package com.example.steamnewsrssviewer.newsdata

data class Appnews(
    val appid: Int,
    val count: Int,
    val newsitems: List<Newsitem>
)