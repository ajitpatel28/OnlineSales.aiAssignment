package com.ajit.onlinesalesaiassignment.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ajit.onlinesalesaiassignment.data.database.AppDatabase
import com.ajit.onlinesalesaiassignment.utils.DummyHistoryItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@ExperimentalCoroutinesApi
class HistoryDaoTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        historyDao = db.historyDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertHistoryItem() = runTest {
        val historyItem = DummyHistoryItem.getDummyHistoryItems().first()
        historyDao.insertHistoryItem(historyItem)

        val allHistoryItems = historyDao.getAllHistoryItems()
        assert(allHistoryItems.contains(historyItem))
    }

    @Test
    fun getAllHistoryItems() = runBlocking {
        val historyItems = DummyHistoryItem.getDummyHistoryItems()

        historyItems.forEach{historyDao.insertHistoryItem(it)}
        val sortedExpectedList = historyItems.sortedBy { it.submissionDate }


        val allHistoryItems = historyDao.getAllHistoryItems()
        val sortedActualList = allHistoryItems.sortedBy { it.submissionDate }

        assertEquals(historyItems.size, allHistoryItems.size)

        // Compare the sorted lists
        assertEquals(sortedExpectedList, sortedActualList)
    }
}
