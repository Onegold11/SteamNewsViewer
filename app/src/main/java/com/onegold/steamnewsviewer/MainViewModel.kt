package com.onegold.steamnewsviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class FragmentName{
    SEARCH, FAVORITE
}

class MainViewModel : ViewModel() {
    val appTitle = MutableLiveData("")
    val appId = MutableLiveData<Int>()
    val currentMenuFragment = MutableLiveData(FragmentName.SEARCH)
}