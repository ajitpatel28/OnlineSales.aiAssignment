package com.ajit.onlinesalesaiassignment.data.repository

import com.ajit.onlinesalesaiassignment.data.api.ExpressionApiService
import com.ajit.onlinesalesaiassignment.data.database.dao.HistoryDao
import com.ajit.onlinesalesaiassignment.data.model.ApiResponse
import com.ajit.onlinesalesaiassignment.data.model.ExpressionRequest
import com.ajit.onlinesalesaiassignment.utils.DummyHistoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class ExpressionRepositoryTest {

    private lateinit var repository: ExpressionRepository
    @Mock
    private lateinit var mockApiService: ExpressionApiService
    @Mock
    private lateinit var historyDao: HistoryDao


    @Before
    fun setUp() {
        mockApiService = mock(ExpressionApiService::class.java)
        historyDao = mock(HistoryDao::class.java)
        repository = ExpressionRepository(mockApiService,historyDao )
    }


    @Test
    fun testEvaluateExpression_Success() = runBlocking {
        val response = ApiResponse(
            result = listOf("7.8", "3.9", "2 inch", "0.5", "3 + 2i", "[[-1, 2], [3, 1]]", "-7"),
            error = null
        )

        val expressions = listOf("a = 1.2*(2 + 4.5)",
            "a / 2",
            "5.08 cm in inch",
            "sin(45 deg) ^ 2",
            "9 / 3 + 2i",
            "b = [-1, 2; 3, 1]",
            "det(b)")
        val request = ExpressionRequest(expressions)

        // Configure mock behavior
        `when`(mockApiService.evaluateExpression(request)).thenAnswer { response }


        // Call the repository method
        val apiResponse = repository.evaluateExpression(expressions)

        // Verify API service interaction
        verify(mockApiService).evaluateExpression(request)

        // Assert the result
        assert(apiResponse.result == response.result)
        assert(apiResponse.error == response.error)
    }


    @Test
    fun testSaveHistoryItem() = runBlockingTest {
        val historyItem = DummyHistoryItem.getDummyHistoryItems().first()

        // Stub the behavior of historyDao.saveHistoryItem
        `when`(historyDao.insertHistoryItem(historyItem)).thenReturn(Unit)


        // Call the repository method
        repository.saveHistoryItem(historyItem)

        val allHistoryItems = listOf(historyItem)
        `when`(repository.getAllHistoryItems()).thenReturn(allHistoryItems)

        // Verify that historyDao.insertHistoryItem is called with the correct argument
        verify(historyDao).insertHistoryItem(historyItem)

        // Call the repository method to get all history items
        val retrievedHistoryItems = repository.getAllHistoryItems()

        // Verify that the retrieved list contains the inserted item
        assertTrue(retrievedHistoryItems.contains(historyItem))

    }


    @Test
    fun testGetAllHistoryItems() = runBlocking {
        val historyItems = DummyHistoryItem.getDummyHistoryItems()
        historyItems.forEach { repository.saveHistoryItem(it) }
        val sortedExpectedList = historyItems.sortedBy { it.submissionDate }

        // Stub historyDao's behavior
        `when`(historyDao.getAllHistoryItems()).thenReturn(historyItems)

        // Call repository's getAllHistoryItems method
        val allHistoryItems = repository.getAllHistoryItems()

        // Verify interaction and assert the result
        verify(historyDao).getAllHistoryItems()
        assert(allHistoryItems == sortedExpectedList)
    }


}
