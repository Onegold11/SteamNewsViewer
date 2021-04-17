package com.example.steamnewsrssviewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.steamnewsrssviewer.database.RoomSteamAppHelper
import com.example.steamnewsrssviewer.database.vo.RoomSteamApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

enum class FragmentName{
    SEARCH, FAVORITE
}

class MainViewModel : ViewModel() {
    val appTitle = MutableLiveData("")
    val appId = MutableLiveData<Int>()
    val currentMenuFragment = MutableLiveData(FragmentName.SEARCH)
}