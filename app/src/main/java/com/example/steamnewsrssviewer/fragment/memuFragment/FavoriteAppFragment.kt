package com.example.steamnewsrssviewer.fragment.memuFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.databinding.FragmentFavoriteAppBinding
import com.example.steamnewsrssviewer.fragment.NewsFragment
import com.example.steamnewsrssviewer.memuFragment.AppRecyclerFragment
import com.example.steamnewsrssviewer.memuFragment.SteamFragment
import com.example.steamnewsrssviewer.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.steamappdb.RoomSteamAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteAppFragment : SteamFragment(), AppRecyclerFragment {
    private lateinit var binding: FragmentFavoriteAppBinding
    private var adapter = SteamAppAdapter(this)
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* view binding */
        binding = FragmentFavoriteAppBinding.inflate(inflater, container, false)

        /* Steam News Recycler view */
        binding.recyclerView3.adapter = adapter
        binding.recyclerView3.layoutManager = LinearLayoutManager(mainActivity)

        /* Set recycler data from database */
        setFavoriteApp()

        return binding.root
    }

    private fun setFavoriteApp() =
        GlobalScope.launch {
            adapter.listData = RoomSteamAppHelper.getFavoriteApp()!!
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }

    override fun requestFragmentChange(result: String) {
        mainActivity?.setFragmentAndSave(NewsFragment(result))
    }
}