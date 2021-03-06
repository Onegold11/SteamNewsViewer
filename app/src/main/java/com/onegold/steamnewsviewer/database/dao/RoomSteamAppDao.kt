package com.onegold.steamnewsviewer.database.dao

import androidx.room.*
import com.onegold.steamnewsviewer.database.vo.RoomSteamApp

@Dao
interface RoomSteamAppDao {
    @Query("select * from orm_steam_app")
    fun getAll(): List<RoomSteamApp>

    @Query("select * from orm_steam_app where name like :title ")
    fun getSteamAppByTitle(title: String): List<RoomSteamApp>

    @Query("select * from orm_steam_app where favorite = :isFavorite")
    fun getFavoriteApp(isFavorite: Boolean): List<RoomSteamApp>

    @Query("select count(*) from orm_steam_app")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(steamApp: RoomSteamApp)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: Iterable<RoomSteamApp>)

    @Update
    fun updateUsers(app: RoomSteamApp)

    @Delete
    fun delete(steamApp: RoomSteamApp)
}