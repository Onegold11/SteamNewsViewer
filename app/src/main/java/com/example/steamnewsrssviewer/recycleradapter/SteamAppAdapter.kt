package com.example.steamnewsrssviewer.recycleradapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.AppRecyclerItemBinding
import com.example.steamnewsrssviewer.memuFragment.AppRecyclerFragment
import com.example.steamnewsrssviewer.steamappdb.RoomSteamAppHelper
import com.example.steamnewsrssviewer.steamappdb.vo.RoomSteamApp

private const val IMAGE_URL_PRE = "https://cdn.akamai.steamstatic.com/steam/apps/"
private const val IMAGE_URL_POST = "/header.jpg"

class SteamAppAdapter(val fragment: AppRecyclerFragment) : RecyclerView.Adapter<SteamAppAdapter.Holder>() {
    var listData: List<RoomSteamApp> = listOf()
    private lateinit var binding: AppRecyclerItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding = AppRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(this.binding, this)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.setNews(listData[position])


    override fun getItemCount(): Int = listData.size


    class Holder(private val binding: AppRecyclerItemBinding, private val adapter: SteamAppAdapter): RecyclerView.ViewHolder(binding.root) {
        private lateinit var newsData: RoomSteamApp

        init {
            /* image click listener */
            binding.imageView.setOnClickListener {
                adapter.fragment.requestFragmentChange(newsData.appid.toString())
            }

            /* favorite button click listener */
            binding.checkBox.setOnClickListener {
                if(binding.checkBox.isChecked){
                    RoomSteamAppHelper.updateFavoriteApp(
                        RoomSteamApp(
                            newsData.appid,
                            newsData.name,
                            true
                        )
                    )
                }else {
                    RoomSteamAppHelper.updateFavoriteApp(
                        RoomSteamApp(
                            newsData.appid,
                            newsData.name,
                            false
                        )
                    )

                }
            }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun setNews(app: RoomSteamApp){
            newsData = app

            /* Set app title in textView */
            binding.textTitle.text = newsData.name

            /* Set app id in textView */
            binding.textAppid.text = "APP ID(${newsData.appid})"

            /* Set imageView using Glide */
            val imageUrl = IMAGE_URL_PRE + app.appid + IMAGE_URL_POST
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.mipmap.ic_error)
                .override(200, 100)
                .into(binding.imageView)

            /* Check if favorite app */
            if (newsData.favorite){
                binding.checkBox.isChecked = true
            }
        }
    }
}