package com.example.steamnewsrssviewer.ui.fragment.memuFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.ui.fragment.NewsFragment
import com.example.steamnewsrssviewer.memuFragment.AppRecyclerFragment
import com.example.steamnewsrssviewer.memuFragment.SteamFragment
import com.example.steamnewsrssviewer.ui.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment(val title: String) : SteamFragment(), AppRecyclerFragment {
    private lateinit var binding: FragmentSearchBinding
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        /* Steam News Recycler view */
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(mainActivity)

        if(this.title != "")
            showSteamAppByTitle(this.title)

        return binding.root
    }

    private fun showSteamAppByTitle(title: String) =
        GlobalScope.launch {
            val queryTitle = "${title}%"

            withContext(Dispatchers.IO) {
                adapter.listData = RoomSteamAppHelper.getSteamAppByTitle(queryTitle)!!
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }


    override fun requestFragmentChange(result: String) {
        mainActivity?.setFragmentAndSave(NewsFragment(result))
    }
}