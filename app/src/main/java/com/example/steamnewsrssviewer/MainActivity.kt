package com.example.steamnewsrssviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.databinding.ItemRecyclerBinding
import com.example.steamnewsrssviewer.newsdata.NewsRecyclerItem
import com.example.steamnewsrssviewer.newsdata.Newsitem
import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        newsUpdate()
    }

    private fun newsUpdate(){
        val data = mutableListOf<NewsRecyclerItem>()

        fun notifyRecyclerDataChanged(){
            this.adapter.listData = data
            this.adapter.notifyDataSetChanged()
        }

        /* Setting retrofit, steam news service */
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        /* Request steam news service */
        val steamNewsService = retrofit.create(SteamNewsService::class.java)
        steamNewsService.getNews().enqueue(object : Callback<SteamNews>{
            /* Success request, send news item to recycler adapter */
            override fun onResponse(
                    call: Call<SteamNews>,
                    response: Response<SteamNews>
            ) {
                val news = response.body() as SteamNews
                val newsId = news.appnews.appid
                val newsCount = news.appnews.count

                /* Collect news */
                val items: List<Newsitem> = news.appnews.newsitems
                for (item in items){
                    data.add(NewsRecyclerItem(title = item.title, date = item.date, url = item.url))
                }

                /* Change recycler data */
                notifyRecyclerDataChanged()
            }

            override fun onFailure(call: Call<SteamNews>, t: Throwable) {
                Log.d("failure", t.printStackTrace().toString())
            }
        })
    }
}