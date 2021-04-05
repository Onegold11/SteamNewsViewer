package com.example.steamnewsrssviewer.memuFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.fragment.NewsFragment
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import kotlinx.coroutines.*

class SearchFragment : SteamAppFragment() {
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

        /* button click listener */
        binding.searchButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                showSteamAppByTitle(binding.searEditText.text.toString())
            }
        }

        return binding.root
    }

    private suspend fun showSteamAppByTitle(title: String) =
        coroutineScope {
            val queryTitle = "${title}%"

            adapter.listData = RoomHelper.getSteamAppByTitle(queryTitle)!!
            adapter.notifyDataSetChanged()
        }


    fun requestFragmentChange(result: String) {
        mainActivity?.setFragment(NewsFragment(result))
    }
}