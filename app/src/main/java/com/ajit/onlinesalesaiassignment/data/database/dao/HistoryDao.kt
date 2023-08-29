package com.ajit.onlinesalesaiassignment.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ajit.onlinesalesaiassignment.data.model.HistoryItem

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryItem(item: HistoryItem)

    @Query("SELECT * FROM history ORDER BY submissionDate DESC")
    suspend fun getAllHistoryItems(): List<HistoryItem>
}
