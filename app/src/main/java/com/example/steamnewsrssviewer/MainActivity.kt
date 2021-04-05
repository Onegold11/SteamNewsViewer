package com.example.steamnewsrssviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.memuFragment.SteamAppFragment
import com.example.steamnewsrssviewer.recycleradapter.NewsAdapter
import com.example.steamnewsrssviewer.retrofitservice.RestfulAdapter
import com.example.steamnewsrssviewer.steamappdata.SteamAppDetailData
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        const val MAIN_TAG = "로그"
    }

    private lateinit var binding: ActivityMainBinding

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
                else -> false
            }
        }

        /* Set content fragment */
        setFragment(SearchFragment())

        /* Steam App DB(singleton) */
        RoomHelper.getSteamAppDao(this)

        /* Request steam to all steam app */
        requestSteamAppList()
    }

    fun setFragment(fragment: SteamAppFragment) = supportFragmentManager.beginTransaction()
        .replace(R.id.frameLayout, fragment)
        .commit()


    /* Search Steam Game and insert database */
    private fun requestSteamAppList() {
        RestfulAdapter.getSteamApi()
            .getSteamAppList().enqueue(object : Callback<SteamAppData> {
                override fun onResponse(
                    call: Call<SteamAppData>,
                    response: Response<SteamAppData>
                ) {
                    val steamApps = response.body() as SteamAppData
                    val list = steamApps.applist.apps.map { RoomSteamApp(it.appid, it.name) }

                    RoomHelper.insertSteamAppsToDB(list)
                }

                override fun onFailure(call: Call<SteamAppData>, t: Throwable) {}
            })
    }

    /* filter steam game only */
    fun searchSteamGame(id: String) {
        RestfulAdapter.getSteamApi()
            .getSteamAppDetail(id)
            .enqueue(object : Callback<Map<String, SteamAppDetailData>> {
                override fun onResponse(
                    call: Call<Map<String, SteamAppDetailData>>,
                    response: Response<Map<String, SteamAppDetailData>>
                ) {
                    val appDetail = response.body()?.get(id)
                    if (appDetail?.data?.type == "game") {

                    }
                }

                override fun onFailure(
                    call: Call<Map<String, SteamAppDetailData>>,
                    t: Throwable
                ) {
                }
            })
    }
}