package com.example.steamnewsrssviewer.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.steamnewsrssviewer.R

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imgView: ImageView, imgUrl: String?){
        imgUrl?.let{
            Glide.with(imgView.context)
                .load(imgUrl)
                .error(R.drawable.ic_baseline_block_24)
                .override(200, 100)
                .into(imgView)
        }
    }
}