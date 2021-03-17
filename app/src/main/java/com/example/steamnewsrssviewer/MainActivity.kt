package com.example.steamnewsrssviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.memuFragment.SteamAppFragment
import com.example.steamnewsrssviewer.newsdata.NewsRecyclerItem
import com.example.steamnewsrssviewer.newsdata.Newsitem
import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.steamappdata.App
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = NewsAdapter()
    private var helper: RoomHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Set Toolbar */
        setSupportActionBar(findViewById(R.id.toolbar))

        /* Set bottom navigation bar */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)

        /* Set content fragment */
        setFragment(SearchFragment())

        /* Steam App DB */
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_steam_app")
                .build()

        /* Steam News Recycler view */
        //binding.recyclerView.adapter = adapter
        //binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //getSteamAppId()
    }

    private fun setFragment(fragment: SteamAppFragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout, fragment)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    private fun getSteamAppId(): List<App> {
        var data = listOf<App>()

        fun insertSteamAppsToDB(data: List<App>) {
            Thread {
                helper?.roomSteamAppDao()?.insertAll(data)
                val lists = helper?.roomSteamAppDao()?.getAll()
                if (lists != null) {
                    var count = 0
                    for (app in lists) {
                        Log.d("Room helper get all", "${app.title} || ${app.appId}")
                        count++

                        if (count > 20)
                            break
                    }
                }
            }.start()
        }

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.steampowered.com/ISteamApps/GetAppList/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        try {
            val steamAppService = retrofit.create(SteamAppService::class.java)
            steamAppService.getSteamApp().enqueue(object : Callback<SteamAppData> {
                override fun onResponse(
                        call: Call<SteamAppData>,
                        response: Response<SteamAppData>
                ) {
                    val steamApps = response.body() as SteamAppData

                    data = steamApps.applist.apps
                    insertSteamAppsToDB(data)
                }

                override fun onFailure(call: Call<SteamAppData>, t: Throwable) {
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }

    private fun getSteamNews() {
        val data = mutableListOf<NewsRecyclerItem>()

        fun notifyRecyclerDataChanged() {
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
        steamNewsService.getNews().enqueue(object : Callback<SteamNews> {
            /* Success request, send news item to recycler adapter */
            override fun onResponse(
                    call: Call<SteamNews>,
                    response: Response<SteamNews>
            ) {
                val news = response.body() as SteamNews

                /* Collect news */
                val items: List<Newsitem> = news.appnews.newsitems
                for (item in items) {
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