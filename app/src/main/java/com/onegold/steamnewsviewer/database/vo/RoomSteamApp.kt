package com.onegold.steamnewsviewer.database.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orm_steam_app")
class RoomSteamApp(
    @PrimaryKey
    @ColumnInfo(name = "id") var appid: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
)