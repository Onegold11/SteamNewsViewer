package com.example.steamnewsrssviewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.steamnewsrssviewer.database.dao.RoomSteamAppDao
import com.example.steamnewsrssviewer.database.vo.RoomSteamApp
import kotlinx.coroutines.*

@Database(entities = [RoomSteamApp::class], version = 1, exportSchema = false)
abstract class RoomSteamAppHelper: RoomDatabase() {
    abstract fun roomSteamAppDao(): RoomSteamAppDao

    companion object {
        private var INSTANCE: RoomSteamAppHelper? = null

        fun getSteamAppDao(context: Context): RoomSteamAppHelper {
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RoomSteamAppHelper::class.java,
                "room_steam_app"
            ).build()
            return INSTANCE!!
        }

        fun destroy() {
            INSTANCE = null
        }


        /* Insert Steam app data(name, id) to database(room) */
        fun insertSteamAppsToDB(data: List<RoomSteamApp>) =
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    INSTANCE?.roomSteamAppDao()?.insertAll(data)
                }
            }

        fun getCount() =
            GlobalScope.launch {
                    INSTANCE?.roomSteamAppDao()?.getCount()
            }

        /* Search app by title */
        suspend fun getSteamAppByTitle(title: String): List<RoomSteamApp>? =
            withContext(Dispatchers.IO) {
                return@withContext INSTANCE?.roomSteamAppDao()?.getSteamAppByTitle(title)
            }

        /* Search app by favorite */
        suspend fun getFavoriteApp(): List<RoomSteamApp>? =
            withContext(Dispatchers.IO){
                return@withContext INSTANCE?.roomSteamAppDao()?.getFavoriteApp(true)
            }


        /* Update favorite */
        fun updateFavoriteApp(app: RoomSteamApp) =
            GlobalScope.launch(Dispatchers.IO){
                INSTANCE?.roomSteamAppDao()?.updateUsers(app)
            }
    }
}