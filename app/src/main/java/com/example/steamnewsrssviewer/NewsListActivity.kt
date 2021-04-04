package com.example.steamnewsrssviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class NewsListActivity : AppCompatActivity() {
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        id = intent.extras?.get("id") as String
    }
}