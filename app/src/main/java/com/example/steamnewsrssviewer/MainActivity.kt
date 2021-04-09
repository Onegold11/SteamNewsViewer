package com.example.steamnewsrssviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.fragment.memuFragment.FavoriteAppFragment
import com.example.steamnewsrssviewer.fragment.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.memuFragment.SteamFragment
import com.example.steamnewsrssviewer.retrofitservice.RestfulAdapter
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.steamappdata.SteamAppDetailData
import com.example.steamnewsrssviewer.steamappdb.RoomSteamAppHelper
import com.example.steamnewsrssviewer.steamappdb.vo.RoomSteamApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val MAIN_TAG = "로그"
    }

    private lateinit var binding: ActivityMainBinding

    private val fragmentStack = Stack<SteamFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Set bottom navigation bar */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.search_menu_item -> {
                    fragmentStack.clear()
                    setFragmentAndSave(SearchFragment())
                    true
                }
                R.id.favorites_menu_item -> {
                    fragmentStack.clear()
                    setFragmentAndSave(FavoriteAppFragment())
                    true
                }
                R.id.library_app_menu_item -> {
                    fragmentStack.clear()
                    true
                }
                else -> false
            }
        }

        /* Set content fragment */
        setFragmentAndSave(SearchFragment())

        /* Steam app DB(singleton) */
        RoomSteamAppHelper.getSteamAppDao(this)

        /* Request steam to all steam app */
        requestSteamAppList()
    }

    override fun onBackPressed() {
        if(fragmentStack.size <= 1) {
            super.onBackPressed()
        }else{
            fragmentStack.pop()
            val previousFragment = fragmentStack.pop()
            setFragment(previousFragment)
            fragmentStack.push(previousFragment)
        }
    }

    fun setFragmentAndSave(fragment: SteamFragment) {
        setFragment(fragment)
        fragmentStack.push(fragment)
    }

    private fun setFragment(fragment: SteamFragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }


    /* Search Steam Game and insert database */
    private fun requestSteamAppList() {
        RestfulAdapter.getSteamApi()
            .getSteamAppList().enqueue(object : Callback<SteamAppData> {
                override fun onResponse(
                    call: Call<SteamAppData>,
                    response: Response<SteamAppData>
                ) {
                    val steamApps = response.body() as SteamAppData
                    val list = steamApps.applist.apps.map { RoomSteamApp(it.appid, it.name, false) }
                    RoomSteamAppHelper.insertSteamAppsToDB(list)
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