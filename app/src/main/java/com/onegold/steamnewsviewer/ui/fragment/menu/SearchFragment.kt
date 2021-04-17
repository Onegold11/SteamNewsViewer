package com.onegold.steamnewsviewer.ui.fragment.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegold.steamnewsviewer.MainViewModel
import com.onegold.steamnewsviewer.R
import com.onegold.steamnewsviewer.databinding.FragmentSearchBinding
import com.onegold.steamnewsviewer.adapter.recycler.SteamAppAdapter
import com.onegold.steamnewsviewer.database.RoomSteamAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment() : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var adapter = SteamAppAdapter(this)
    private lateinit var viewModel: MainViewModel
    private var pressBackTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentTime = System.currentTimeMillis()
            val intervalTime = currentTime - pressBackTime

            if (intervalTime in 0..2000) {
                activity?.finish()
            } else {
                pressBackTime = currentTime
                Toast.makeText(activity, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        showSteamAppList()
    }

    private fun showSteamAppList() =
        GlobalScope.launch {
            val query = "%${viewModel.appTitle.value}%"
            adapter.listData = RoomSteamAppHelper.getSteamAppByTitle(query)!!
            Log.d("size", "${adapter.listData.size}")
            if(adapter.listData.size < 50){
                adapter.listData.forEach { it -> Log.d("title-id", "${it.name}/${it.appid}") }
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
}