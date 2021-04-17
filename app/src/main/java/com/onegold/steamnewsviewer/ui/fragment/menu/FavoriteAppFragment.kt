package com.onegold.steamnewsviewer.ui.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.onegold.steamnewsviewer.R
import com.onegold.steamnewsviewer.databinding.FragmentFavoriteAppBinding
import com.onegold.steamnewsviewer.adapter.recycler.SteamAppAdapter
import com.onegold.steamnewsviewer.database.RoomSteamAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteAppFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteAppBinding
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
    ): View? {

        return inflater.inflate(R.layout.fragment_favorite_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!
        binding.recyclerView3.adapter = adapter
        binding.recyclerView3.layoutManager = LinearLayoutManager(activity)

        setFavoriteApp()
    }

    private fun setFavoriteApp() =
        GlobalScope.launch {
            adapter.listData = RoomSteamAppHelper.getFavoriteApp()!!
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
}