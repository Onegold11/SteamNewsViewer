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
import com.example.steamnewsrssviewer.steamappdb.RoomSteamAppHelper
import kotlinx.coroutines.*

class SearchFragment : SteamFragment(), AppRecyclerFragment {
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
            showSteamAppByTitle(binding.searEditText.text.toString())
        }

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