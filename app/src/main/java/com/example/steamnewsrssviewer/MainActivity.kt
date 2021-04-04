package com.example.steamnewsrssviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.memuFragment.SteamAppFragment
import com.example.steamnewsrssviewer.newsdata.NewsRecyclerItem
import com.example.steamnewsrssviewer.newsdata.Newsitem
import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.recycleradapter.NewsAdapter
import com.example.steamnewsrssviewer.retrofitservice.RestfulAdapter
import com.example.steamnewsrssviewer.retrofitservice.SteamAppDetailService
import com.example.steamnewsrssviewer.retrofitservice.SteamAppService
import com.example.steamnewsrssviewer.retrofitservice.SteamNewsService
import com.example.steamnewsrssviewer.steamappdata.App
import com.example.steamnewsrssviewer.steamappdata.SteamAppDetailData
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object {
        const val MAIN_TAG = "로그"
        const val STEAM_APP_LIST_URL = "https://api.steampowered.com/ISteamApps/GetAppList/v2/"
        const val STEAM_DETAIL_URL = "https://store.steampowered.com/api/"
    }

    private lateinit var binding: ActivityMainBinding
    private val adapter = NewsAdapter()
    private var helper: RoomHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Set bottom navigation bar */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.favorites_menu_item -> {
                    true
                }
                R.id.search_menu_item -> {
                    setFragment(SearchFragment())
                    true
                }
                R.id.library_app_menu_item -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

        /* Set content fragment */
        setFragment(SearchFragment())

        /* Steam App DB */
        helper = RoomHelper.getSteamAppDao(this)

        requestSteamAppList()
    }

    private fun setFragment(fragment: SteamAppFragment) = supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()


    /* Search Steam Game and insert database */
    private fun requestSteamAppList() {
        RestfulAdapter
            .getSteamAppListApi()
            .getSteamAppList().enqueue(object : Callback<SteamAppData> {
                override fun onResponse(
                    call: Call<SteamAppData>,
                    response: Response<SteamAppData>
                ) {
                    val steamApps = response.body() as SteamAppData
                    val list = steamApps.applist.apps.map { RoomSteamApp(it.appid, it.name) }

                    helper?.insertSteamAppsToDB(list)
                }

                override fun onFailure(call: Call<SteamAppData>, t: Throwable) {}
            })
    }

    /* filter steam game only */
    fun searchSteamGame(id: String) {
        RestfulAdapter
            .getSteamAppDetailApi()
            .getSteamAppDetail(id)
            .enqueue(object : Callback<Map<String, SteamAppDetailData>> {
                override fun onResponse(
                    call: Call<Map<String, SteamAppDetailData>>,
                    response: Response<Map<String, SteamAppDetailData>>
                ) {
                    val appDetail = response.body()?.get(id)
                    Log.d(MAIN_TAG, "Found start $appDetail")
                    if (appDetail?.data?.type == "game") {
                        Log.d(MAIN_TAG, "Found Game")
                    }
                    Log.d(MAIN_TAG, "Found terminate")
                }

                override fun onFailure(
                    call: Call<Map<String, SteamAppDetailData>>,
                    t: Throwable
                ) {
                    Log.d(MAIN_TAG, t.message.toString())
                }
            })
    }

    private fun getSteamNews() {
        val data = mutableListOf<NewsRecyclerItem>()

        fun notifyRecyclerDataChanged() {
            this.adapter.listData = data
            this.adapter.notifyDataSetChanged()
        }

        /* Setting retrofit, steam news service */
        RestfulAdapter
            .getSteamNewsApi()
            .getNews().enqueue(object : Callback<SteamNews> {
                /* Success request, send news item to recycler adapter */
                override fun onResponse(
                    call: Call<SteamNews>,
                    response: Response<SteamNews>
                ) {
                    val news = response.body() as SteamNews

                    /* Collect news */
                    val items: List<Newsitem> = news.appnews.newsitems
                    for (item in items) {
                        data.add(
                            NewsRecyclerItem(
                                title = item.title,
                                date = item.date,
                                url = item.url
                            )
                        )
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