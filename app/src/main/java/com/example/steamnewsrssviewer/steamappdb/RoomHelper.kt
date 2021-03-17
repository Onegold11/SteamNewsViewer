package com.example.steamnewsrssviewer.steamappdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomSteamApp::class], version = 1, exportSchema = false)
abstract class RoomHelper: RoomDatabase() {
    abstract fun roomSteamAppDao(): RoomSteamAppDao
}