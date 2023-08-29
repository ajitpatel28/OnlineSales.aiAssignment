package com.ajit.onlinesalesaiassignment.utils

import com.ajit.onlinesalesaiassignment.data.model.HistoryItem

object DummyHistoryItem {
    fun getDummyHistoryItems(): List<HistoryItem> {
        return listOf(
            HistoryItem(1, "2+2", "4", System.currentTimeMillis() - 10000),
            HistoryItem(2, "3*5", "15", System.currentTimeMillis() - 5000),
            HistoryItem(3, "10-6", "4", System.currentTimeMillis())
        )
    }
}
