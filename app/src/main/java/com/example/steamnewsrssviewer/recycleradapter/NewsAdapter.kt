package com.example.steamnewsrssviewer.recycleradapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.NewsRecyclerItemBinding
import com.example.steamnewsrssviewer.newsdata.NewsRecyclerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.Holder>(){
    var listData: List<NewsRecyclerItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = NewsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val news = listData[position]
        holder.setNews(news)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class Holder(itemView: NewsRecyclerItemBinding, parent: ViewGroup) : RecyclerView.ViewHolder(itemView.root) {
        private val view: NewsRecyclerItemBinding = itemView
        private lateinit var newsData: NewsRecyclerItem

        init {
            view.item.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(newsData.url)
                parent.context.startActivity(intent)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun setNews(news: NewsRecyclerItem){
            newsData = news
            view.textTitle.text = newsData.title

            /* convert UNIX timestamp to string */
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.format(Date(newsData.date.toLong() * 1000))
            view.textDate.text = date.toString()

            /* Set preview image */
            GlobalScope.launch {
                val previewUrl = getPreviewImageUrl()

                withContext(Dispatchers.Main) {
                    Glide.with(itemView)
                        .load(previewUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_error)
                        .into(view.imageView2)
                }
            }
        }

        suspend fun getPreviewImageUrl(): String{
            var imageUrl: String
            withContext(Dispatchers.IO){
                try {
                    val doc = Jsoup.connect(newsData.url).get()
                    imageUrl = doc.select("meta[property=og:image]")[0].attr("content")
                }catch (e: Exception){
                    imageUrl = ""
                }
            }
            return imageUrl
        }
    }
}