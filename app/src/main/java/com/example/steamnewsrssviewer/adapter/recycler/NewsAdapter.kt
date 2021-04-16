package com.example.steamnewsrssviewer.adapter.recycler

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.NewsRecyclerItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

data class NewsRecyclerItem (val title: String, val date: Int, val url: String)

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
    var listData: List<NewsRecyclerItem> = listOf()
    private lateinit var binding: NewsRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding =
            NewsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(this.binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.setNews(listData[position])

    override fun getItemCount(): Int = listData.size


    class Holder(private val binding: NewsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = MutableLiveData<String>()
        val date = MutableLiveData<String>()
        val imgUrl = MutableLiveData<String>()
        private val url = MutableLiveData<String>()

        init {
            binding.holder = this

            binding.item.setOnClickListener {view ->
                url.value?.let {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(it)
                    view.context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun setNews(news: NewsRecyclerItem) {
            this.title.value = news.title
            this.url.value = news.url

            /* convert UNIX timestamp to string */
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.format(Date(news.date.toLong() * 1000))
            this.date.value = date.toString()


            /* Set preview image */
            GlobalScope.launch(Dispatchers.Main) {
                imgUrl.value = getPreviewImageUrl(news.url)
            }
        }

        private suspend fun getPreviewImageUrl(url: String): String =
            withContext(Dispatchers.IO) {
                return@withContext try {
                    val doc = Jsoup.connect(url).get()
                    doc.select("meta[property=og:image]")[0].attr("content")
                } catch (e: Exception) {
                    ""
                }
            }

    }
}