package com.example.steamnewsrssviewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

enum class FragmentName{
    SEARCH, FAVORITE, ALLNEWS, NEWS, NEWSDETAIL
}

class MainViewModel : ViewModel() {
    val appTitle = MutableLiveData("")
    val appId = MutableLiveData<Int>()
    val currentMenuFragment = MutableLiveData(FragmentName.SEARCH)

    fun changeMenu(){

    }
}