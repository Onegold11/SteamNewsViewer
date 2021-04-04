package com.example.steamnewsrssviewer.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.steamnewsrssviewer.MainActivity
import com.example.steamnewsrssviewer.R
import com.example.steamnewsrssviewer.databinding.FragmentNewsBinding
import com.example.steamnewsrssviewer.databinding.FragmentSearchBinding
import com.example.steamnewsrssviewer.memuFragment.SteamAppFragment
import com.example.steamnewsrssviewer.newsdata.NewsRecyclerItem
import com.example.steamnewsrssviewer.newsdata.Newsitem
import com.example.steamnewsrssviewer.newsdata.SteamNews
import com.example.steamnewsrssviewer.recycleradapter.NewsAdapter
import com.example.steamnewsrssviewer.recycleradapter.SteamAppAdapter
import com.example.steamnewsrssviewer.retrofitservice.RestfulAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : SteamAppFragment() {
    private lateinit var binding: FragmentNewsBinding
    private var mainActivity: MainActivity? = null
    private var adapter = NewsAdapter()
    private var newsId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey"){ _, bundle ->
            newsId = bundle.getString("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {/* view binding */
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(mainActivity)

        getSteamNews()

        return binding.root
    }

    private fun getSteamNews() {
        val data = mutableListOf<NewsRecyclerItem>()

        fun notifyRecyclerDataChanged() {
            this.adapter.listData = data
            this.adapter.notifyDataSetChanged()
        }

        /* Setting retrofit, steam news service */
        RestfulAdapter
            .getSteamNewsApi()
            .getNews().enqueue(object : Callback<SteamNews> {
                /* Success request, send news item to recycler adapter */
                override fun onResponse(
                    call: Call<SteamNews>,
                    response: Response<SteamNews>
                ) {
                    val news = response.body() as SteamNews

                    /* Collect news */
                    val items: List<Newsitem> = news.appnews.newsitems
                    for (item in items) {
                        data.add(
                            NewsRecyclerItem(
                                title = item.title,
                                date = item.date,
                                url = item.url
                            )
                        )
                    }

                    /* Change recycler data */
                    notifyRecyclerDataChanged()
                }

                override fun onFailure(call: Call<SteamNews>, t: Throwable) {}
            })
    }
}