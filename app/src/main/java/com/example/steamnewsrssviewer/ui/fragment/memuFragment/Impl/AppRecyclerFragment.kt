package com.example.steamnewsrssviewer.memuFragment

import androidx.fragment.app.Fragment

interface AppRecyclerFragment {
    fun requestFragmentChange(result: String)
}

abstract class SteamFragment : Fragment() {
}