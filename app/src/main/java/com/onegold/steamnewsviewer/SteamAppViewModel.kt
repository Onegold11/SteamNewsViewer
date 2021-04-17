package com.onegold.steamnewsviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SteamAppViewModel : ViewModel() {
    val title = MutableLiveData<String>()
    val id = MutableLiveData<Int>()
    val imgUrl = MutableLiveData<String>()
    val isFavorite = MutableLiveData<Boolean>()
}