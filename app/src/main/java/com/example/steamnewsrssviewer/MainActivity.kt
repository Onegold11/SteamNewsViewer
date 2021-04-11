package com.example.steamnewsrssviewer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.steamnewsrssviewer.ui.fragment.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.memuFragment.SteamFragment
import com.example.steamnewsrssviewer.network.RestfulAdapter
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import com.example.steamnewsrssviewer.database.vo.RoomSteamApp
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(){
    private val fragmentStack = Stack<SteamFragment>()
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayoutComponentView()

        initDatabaseAndRequestSteamApp()
    }

    private fun setLayoutComponentView(){
        /* Navigation Drawer and Tool bar */
        setNavigationDrawerAndToolbar()

        /* Set content fragment */
        setFragmentAndSave(SearchFragment(""))
    }

    private fun setNavigationDrawerAndToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.nav_search -> {

                }
                R.id.nav_favorite -> {

                }
                R.id.nav_news -> {

                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setCheckedItem(R.id.nav_search)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val result = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val mSearch = menu?.findItem(R.id.app_bar_search)
        val searchView = mSearch?.actionView as SearchView
        setQueryTextListener(searchView)

        return result
    }

    private fun setQueryTextListener(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                setFragment(SearchFragment(query!!))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun initDatabaseAndRequestSteamApp(){
        /* Steam app DB(singleton) */
        RoomSteamAppHelper.getSteamAppDao(this)

        /* Request steam to all steam app */
        requestSteamAppList()
    }


    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
          drawer.closeDrawer(GravityCompat.START)
        } else if(fragmentStack.size <= 1) {
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
}