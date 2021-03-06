package com.example.steamnewsrssviewer

import androidx.room.*

@Dao
interface RoomSteamAppDao {
    @Query("select * from orm_steam_app")
    fun getAll(): List<RoomSteamApp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(steamApp: RoomSteamApp)

    @Delete
    fun delete(steamApp: RoomSteamApp)
}