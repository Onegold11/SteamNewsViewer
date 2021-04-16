package com.example.steamnewsrssviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.steamnewsrssviewer.ui.fragment.menu.SearchFragment
import com.example.steamnewsrssviewer.network.RestfulAdapter
import com.example.steamnewsrssviewer.steamappdata.SteamAppData
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import com.example.steamnewsrssviewer.database.vo.RoomSteamApp
import com.example.steamnewsrssviewer.databinding.ActivityMainBinding
import com.example.steamnewsrssviewer.ui.fragment.NewsFragment
import com.example.steamnewsrssviewer.ui.fragment.menu.FavoriteAppFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()

        setNavigationDrawerAndToolbar()

        initDatabaseAndRequestSteamApp()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.currentMenuFragment.observe(this, {
            when (it) {
                FragmentName.SEARCH -> {
                    setFragment(SearchFragment())
                }
                FragmentName.FAVORITE -> {
                    setFragment(FavoriteAppFragment())
                }
                else -> {

                }
            }
        })
        viewModel.appId.observe(this, {
            setFragment(NewsFragment(it.toString()))
        })
    }

    private fun setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
    }

    private fun setNavigationDrawerAndToolbar() {
        setSupportActionBar(binding.appBar.toolbar)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_search -> {
                    viewModel.currentMenuFragment.value = FragmentName.SEARCH
                }
                R.id.nav_favorite -> {
                    viewModel.currentMenuFragment.value = FragmentName.FAVORITE
                }
                R.id.nav_source -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/Onegold11/SteamNewsViewer")
                    startActivity(intent)
                }
                R.id.nav_license -> {
                    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.appBar.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setCheckedItem(R.id.nav_search)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val result = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val mSearch = menu?.findItem(R.id.app_bar_search)
        val searchView = mSearch?.actionView as SearchView
        setQueryTextListener(searchView)

        return result
    }

    private fun setQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.appTitle.value = query
                setFragment(SearchFragment())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun initDatabaseAndRequestSteamApp() {
        /* Steam app DB(singleton) */
        RoomSteamAppHelper.getSteamAppDao(this)

        /* Request steam to all steam app */
        requestSteamAppList()
    }


    private fun setFragment(fragment: Fragment) =
        with(supportFragmentManager) {
            /* Remove same fragment */
            /* Replace fragment and save back stack */
            beginTransaction()
                .replace(R.id.frameLayout, fragment).apply {
                    addToBackStack(null)
                    commit()
                }
        }


    /* Search Steam Game and insert database */
    private fun requestSteamAppList() {
        RestfulAdapter.getSteamApi()
            .getSteamAppList().enqueue(object : Callback<SteamAppData> {
                override fun onResponse(
                    call: Call<SteamAppData>,
                    response: Response<SteamAppData>
                ) {
                    response.body()?.let { app ->
                        val list =
                            app.applist.apps.map { RoomSteamApp(it.appid, it.name, false) }
                        RoomSteamAppHelper.insertSteamAppsToDB(list)
                    }
                }

                override fun onFailure(call: Call<SteamAppData>, t: Throwable) {}
            })
    }
}