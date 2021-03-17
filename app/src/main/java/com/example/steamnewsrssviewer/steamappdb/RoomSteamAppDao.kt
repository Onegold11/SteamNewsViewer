package com.example.steamnewsrssviewer.steamappdb

import androidx.room.*
import com.example.steamnewsrssviewer.steamappdata.App

@Dao
interface RoomSteamAppDao {
    @Query("select * from orm_steam_app")
    fun getAll(): List<RoomSteamApp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(steamApp: RoomSteamApp)

    @Transaction
    fun insertAll(steamApps: List<App>){
        for(data in steamApps){
            insert(RoomSteamApp(data.appid, data.name))
        }
    }

    @Delete
    fun delete(steamApp: RoomSteamApp)
}