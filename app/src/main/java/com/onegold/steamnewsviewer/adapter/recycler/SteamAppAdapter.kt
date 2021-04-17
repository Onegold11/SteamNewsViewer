package com.onegold.steamnewsviewer.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onegold.steamnewsviewer.MainViewModel
import com.onegold.steamnewsviewer.R
import com.onegold.steamnewsviewer.databinding.AppRecyclerItemBinding
import com.onegold.steamnewsviewer.database.RoomSteamAppHelper
import com.onegold.steamnewsviewer.database.vo.RoomSteamApp

private const val IMAGE_URL_PRE = "https://cdn.akamai.steamstatic.com/steam/apps/"
private const val IMAGE_URL_POST = "/header.jpg"

class SteamAppAdapter(val fragment: Fragment) : RecyclerView.Adapter<SteamAppAdapter.Holder>() {
    var listData: List<RoomSteamApp> = listOf()
    private lateinit var binding: AppRecyclerItemBinding


    class Holder(
        binding: AppRecyclerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.textTitle
        val id: TextView = binding.textAppid
        val image: ImageView = binding.imageView
        val favorite: CheckBox = binding.checkBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding =
            AppRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(this.binding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = listData[position].name
        holder.id.text = listData[position].appid.toString()
        holder.favorite.isChecked = listData[position].favorite

        Glide.with(fragment.requireActivity())
            .load(IMAGE_URL_PRE + listData[position].appid + IMAGE_URL_POST)
            .error(R.drawable.ic_baseline_block_24)
            .override(200, 100)
            .into(holder.image)

        /* image click listener */
        holder.image.setOnClickListener {
            val viewModel = ViewModelProvider(fragment.requireActivity()).get(MainViewModel::class.java)
            viewModel.appId.value = listData[position].appid
        }

        /* favorite button click listener */
        holder.favorite.setOnClickListener {
            RoomSteamAppHelper.updateFavoriteApp(
                RoomSteamApp(
                    listData[position].appid,
                    listData[position].name,
                    !listData[position].favorite
                )
            )
        }
    }

    override fun getItemCount(): Int = listData.size
}