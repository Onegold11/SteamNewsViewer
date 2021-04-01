package com.example.steamnewsrssviewer.recycleradapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.AppRecyclerItemBinding
import com.example.steamnewsrssviewer.steamappdata.App
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp

class SteamAppAdapter : RecyclerView.Adapter<SteamAppAdapter.Holder>() {
    var listData: List<RoomSteamApp> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AppRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val news = listData[position]
        holder.setNews(news)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class Holder(itemView: AppRecyclerItemBinding, parent: ViewGroup) : RecyclerView.ViewHolder(itemView.root) {
        private val IMAGE_URL_PRE = "https://cdn.akamai.steamstatic.com/steam/apps/"
        private val IMAGE_URL_POST = "/header.jpg"
        private val view: AppRecyclerItemBinding = itemView
        private lateinit var newsData: RoomSteamApp

        init {
            view.item.setOnClickListener {
                Log.d(MainActivity.MAIN_TAG, "${newsData.title}/${newsData.appId}")
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun setNews(app: RoomSteamApp){
            newsData = app

            /* Set app title in textView */
            view.textTitle.text = newsData.title

            /* Set app id in textView */
            view.textId.text = "App ID : ${newsData.appId}"

            /* Set imageView using Glide */
            val imageUrl = IMAGE_URL_PRE + app.appId + IMAGE_URL_POST
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_error)
                .into(view.imageView)
        }
    }
}