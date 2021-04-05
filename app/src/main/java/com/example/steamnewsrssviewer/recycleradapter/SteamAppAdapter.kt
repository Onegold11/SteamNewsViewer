package com.example.steamnewsrssviewer.recycleradapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.AppRecyclerItemBinding
import com.example.steamnewsrssviewer.memuFragment.SearchFragment
import com.example.steamnewsrssviewer.steamappdb.RoomSteamApp

private const val IMAGE_URL_PRE = "https://cdn.akamai.steamstatic.com/steam/apps/"
private const val IMAGE_URL_POST = "/header.jpg"

class SteamAppAdapter(fragment: SearchFragment) : RecyclerView.Adapter<SteamAppAdapter.Holder>() {
    var listData: List<RoomSteamApp> = mutableListOf()
    var fragment = fragment


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AppRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, this)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val news = listData[position]
        holder.setNews(news)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class Holder(itemView: AppRecyclerItemBinding, root: SteamAppAdapter) : RecyclerView.ViewHolder(itemView.root) {
        private val view: AppRecyclerItemBinding = itemView
        private lateinit var newsData: RoomSteamApp

        init {
            /* image click listener */
            view.imageView.setOnClickListener {
                root.fragment.requestFragmentChange(newsData.appid.toString())
            }

            /* favorite button click listener */
            view.checkBox.setOnClickListener {
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun setNews(app: RoomSteamApp){
            newsData = app

            /* Set app title in textView */
            view.textTitle.text = "${newsData.name}"

            view.textAppid.text = "APP ID(${newsData.appid})"

            /* Set imageView using Glide */
            val imageUrl = IMAGE_URL_PRE + app.appid + IMAGE_URL_POST
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_error)
                .into(view.imageView)
        }
    }
}