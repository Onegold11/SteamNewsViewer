package com.example.steamnewsrssviewer.adapter.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.steamnewsrssviewer.MainViewModel
import com.example.steamnewsrssviewer.databinding.AppRecyclerItemBinding
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import com.example.steamnewsrssviewer.database.vo.RoomSteamApp

private const val IMAGE_URL_PRE = "https://cdn.akamai.steamstatic.com/steam/apps/"
private const val IMAGE_URL_POST = "/header.jpg"

class SteamAppAdapter(val fragment: Fragment) : RecyclerView.Adapter<SteamAppAdapter.Holder>() {
    var listData: List<RoomSteamApp> = listOf()
    private lateinit var binding: AppRecyclerItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding =
            AppRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(this.binding, this)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.setNews(listData[position])


    override fun getItemCount(): Int = listData.size


    class Holder(
        private val binding: AppRecyclerItemBinding,
        private val adapter: SteamAppAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        val title = MutableLiveData<String>()
        val id = MutableLiveData<Int>()
        val imgUrl = MutableLiveData<String>()
        val isFavorite = MutableLiveData<Boolean>()

        init {
            binding.holder = this

            /* image click listener */
            binding.imageView.setOnClickListener {
                val viewModel = ViewModelProvider(adapter.fragment.requireActivity()).get(MainViewModel::class.java)
                viewModel.appId.value = this.id.value
            }

            /* favorite button click listener */
            binding.checkBox.setOnClickListener {
                RoomSteamAppHelper.updateFavoriteApp(
                    RoomSteamApp(
                        this.id.value!!,
                        this.title.value!!,
                        !this.isFavorite.value!!
                    )
                )
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun setNews(app: RoomSteamApp) {
            this.title.value = app.name
            this.id.value = app.appid
            this.imgUrl.value = IMAGE_URL_PRE + app.appid + IMAGE_URL_POST
            this.isFavorite.value = app.favorite
        }
    }
}