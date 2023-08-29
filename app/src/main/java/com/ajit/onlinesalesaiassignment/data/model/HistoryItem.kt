package com.ajit.onlinesalesaiassignment.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "expression") val expression: String,
    @ColumnInfo(name = "result") val result: String,
    @ColumnInfo(name = "submissionDate") val submissionDate: Long = System.currentTimeMillis()
)
