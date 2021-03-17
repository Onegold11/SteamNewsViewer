package com.example.steamnewsrssviewer.steamappdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orm_steam_app")
class RoomSteamApp(
    @PrimaryKey
    @ColumnInfo var appId: Int, @ColumnInfo var title: String
) {

}