package com.example.steamnewsrssviewer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orm_steam_app")
class RoomSteamApp(@ColumnInfo var title: String, @ColumnInfo var appId: Long) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var no: Long? = null

}