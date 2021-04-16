package com.example.steamnewsrssviewer.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.databinding.FragmentNewsBinding
import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.adapter.recycler.NewsAdapter
import com.example.steamnewsrssviewer.adapter.recycler.NewsRecyclerItem
import com.example.steamnewsrssviewer.network.RestfulAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment(private val id: String) : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var mainActivity: MainActivity
    private var adapter = NewsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(mainActivity)

        getSteamNews()

        return binding.root
    }

    private fun getSteamNews() {
        /* Setting retrofit, steam news service */
        RestfulAdapter.getSteamApi()
            .getNews(id).enqueue(object : Callback<SteamNews> {
                /* Success request, send news item to recycler adapter */
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(
                    call: Call<SteamNews>,
                    response: Response<SteamNews>
                ) {
                    response.body()?.let { news ->
                        /* Collect news */
                        val items = news.appnews.newsitems.map {
                            NewsRecyclerItem(it.title, it.date, it.url)
                        }

                        adapterNotifyDataChange(items)
                    }
                }

                override fun onFailure(call: Call<SteamNews>, t: Throwable) {}
            })
    }

    private fun adapterNotifyDataChange(items: List<NewsRecyclerItem>) {
        adapter.listData = items

        GlobalScope.launch(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }
}