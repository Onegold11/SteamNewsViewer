package com.example.steamnewsrssviewer.memuFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.steamappdata.App
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp
import kotlinx.coroutines.*

class SearchFragment : SteamAppFragment() {
    private lateinit var binding: FragmentSearchBinding
    private var adapter = SteamAppAdapter()
    private var mainActivity: MainActivity? = null
    private var helper: RoomHelper? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

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
        val view = binding.root

        /* Steam News Recycler view */
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(mainActivity)

        /* button click listener */
        binding.searchButton.setOnClickListener {
            scope.launch {
                showSteamAppByTitle(binding.searEditText.text.toString())
            }
        }

        helper = RoomHelper.getSteamAppDao(mainActivity as Context)

        return view
    }

    private suspend fun showSteamAppByTitle(title: String) =
        coroutineScope {
            val queryTitle = "${title}%"

            adapter.listData = helper?.getSteamAppByTitle(queryTitle)!!
            adapter.notifyDataSetChanged()
        }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) {}
    }
}