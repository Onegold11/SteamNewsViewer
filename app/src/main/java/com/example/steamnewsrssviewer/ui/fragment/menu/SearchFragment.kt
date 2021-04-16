package com.example.steamnewsrssviewer.ui.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainViewModel
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.adapter.recycler.SteamAppAdapter
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment() : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var adapter = SteamAppAdapter(this)
    private var pressBackTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentTime = System.currentTimeMillis()
            val intervalTime = currentTime - pressBackTime

            if(intervalTime in 0..2000){
                activity?.finish()
            }else{
                pressBackTime = currentTime
                Toast.makeText(activity, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.appTitle.value?.let {
            showSteamAppByTitle(it)
        }

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
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

}