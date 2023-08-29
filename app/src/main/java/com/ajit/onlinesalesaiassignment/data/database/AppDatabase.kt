package com.ajit.onlinesalesaiassignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ajit.onlinesalesaiassignment.data.database.dao.HistoryDao
import com.ajit.onlinesalesaiassignment.data.model.HistoryItem

    @Database(entities = [HistoryItem::class], version = 1, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun historyDao(): HistoryDao
    }
