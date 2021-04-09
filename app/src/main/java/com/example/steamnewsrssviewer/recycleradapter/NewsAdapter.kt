package com.example.steamnewsrssviewer.recycleradapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.NewsRecyclerItemBinding
import com.example.steamnewsrssviewer.recycleradapter.data.NewsRecyclerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
    var listData: List<NewsRecyclerItem> = listOf()
    private lateinit var binding: NewsRecyclerItemBinding
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding =
            NewsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.parent = parent
        return Holder(this.binding, this.parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.setNews(listData[position])

    override fun getItemCount(): Int = listData.size


    class Holder(private val binding: NewsRecyclerItemBinding, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var newsData: NewsRecyclerItem

        init {
            binding.item.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(newsData.url)
                parent.context.startActivity(intent)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun setNews(news: NewsRecyclerItem) {
            newsData = news
            binding.textTitle.text = newsData.title

            /* convert UNIX timestamp to string */
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.format(Date(newsData.date.toLong() * 1000))
            binding.textDate.text = date.toString()


            /* Set preview image */
            GlobalScope.launch(Dispatchers.Main) {
                val imageUrl = getPreviewImageUrl(newsData.url)

                /* Preview image not exist */
                if(imageUrl != ""){
                    Glide.with(itemView)
                        .load(imageUrl)
                        .error(R.mipmap.ic_error)
                        .override(200, 100)
                        .into(binding.imageView2)
                }else{
                    Glide.with(itemView)
                        .load(R.drawable.ic_baseline_no_image_24)
                        .override(200, 100)
                        .into(binding.imageView2)
                }
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