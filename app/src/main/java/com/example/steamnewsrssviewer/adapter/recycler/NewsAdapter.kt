package com.example.steamnewsrssviewer.adapter.recycler

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
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

class NewsAdapter(val fragment: Fragment) : RecyclerView.Adapter<NewsAdapter.Holder>() {
    var listData: List<NewsRecyclerItem> = listOf()
    private lateinit var binding: NewsRecyclerItemBinding

    class Holder(private val binding: NewsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.textTitle
        val date: TextView = binding.textDate
        val image: ImageView = binding.imageView2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.binding =
            NewsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(this.binding)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = listData[position].title
        holder.date.text = SimpleDateFormat("yyyy-MM-dd").format(Date(listData[position].date.toLong() * 1000)).toString()

        /* Set preview image */
        GlobalScope.launch(Dispatchers.Main) {
            Glide.with(fragment.requireActivity())
                .load(getPreviewImageUrl(listData[position].url))
                .error(R.drawable.ic_baseline_block_24)
                .override(200, 100)
                .into(holder.image)
        }
        holder.image.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(listData[position].url)
            it.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = listData.size

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