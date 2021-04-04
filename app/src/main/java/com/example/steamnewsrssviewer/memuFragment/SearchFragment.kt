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
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.fragment.NewsFragment
import com.example.steamnewsrssviewer.steamappdata.App
import com.example.steamnewsrssviewer.steamappdb.RoomHelper
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class SearchFragment : SteamAppFragment() {
    private lateinit var binding: FragmentSearchBinding
    private var adapter = SteamAppAdapter(this)
    private var mainActivity: MainActivity? = null
    private var helper: RoomHelper? = null

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

        helper = RoomHelper.getSteamAppDao(mainActivity as Context)

        return binding.root
    }

    private suspend fun showSteamAppByTitle(title: String) =
        coroutineScope {
            val queryTitle = "${title}%"

            adapter.listData = helper?.getSteamAppByTitle(queryTitle)!!
            adapter.notifyDataSetChanged()
        }

    fun setFragmentResult(result: String) {
        setFragmentResult("requestKey", bundleOf("id" to result))
        requestFragmentChange()
    }

    private fun requestFragmentChange() {
        mainActivity?.setFragment(NewsFragment())
    }
}