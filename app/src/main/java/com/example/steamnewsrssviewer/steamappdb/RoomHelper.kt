package com.example.steamnewsrssviewer.steamappdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.steamnewsrssviewer.steamappdata.App
import kotlinx.coroutines.*

@Database(entities = [RoomSteamApp::class], version = 1, exportSchema = false)
abstract class RoomHelper: RoomDatabase() {
    abstract fun roomSteamAppDao(): RoomSteamAppDao

    companion object {
        private var INSTANCE: RoomHelper? = null

        fun getSteamAppDao(context: Context): RoomHelper{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(context.applicationContext, RoomHelper::class.java, "room_steam_app")
                    .build()
            return INSTANCE!!
        }

        fun destroy(){
            INSTANCE = null
        }
    }

    /* Insert Steam app data(name, id) to database(room) */
    fun insertSteamAppsToDB(data: List<App>) =
        GlobalScope.launch {
            INSTANCE?.roomSteamAppDao()?.insertAll(data)
        }

    suspend fun getSteamAppByTitle(title: String): List<RoomSteamApp>? {
        var result: List<RoomSteamApp>? = null
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            result = INSTANCE?.roomSteamAppDao()?.getSteamAppByTitle(title)
        }
        return result
    }
}