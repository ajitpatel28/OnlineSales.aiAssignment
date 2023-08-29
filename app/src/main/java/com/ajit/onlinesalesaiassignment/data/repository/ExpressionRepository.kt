package com.ajit.onlinesalesaiassignment.data.repository

import com.ajit.onlinesalesaiassignment.data.api.ExpressionApiService
import com.ajit.onlinesalesaiassignment.data.database.dao.HistoryDao
import com.ajit.onlinesalesaiassignment.data.model.ApiResponse
import com.ajit.onlinesalesaiassignment.data.model.ExpressionRequest
import com.ajit.onlinesalesaiassignment.data.model.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpressionRepository @Inject constructor(
    private val apiService: ExpressionApiService,
    private val historyDao: HistoryDao
) {

    /**
     * Evaluate expressions using the API service.
     *
     * @param expressions List of expressions to be evaluated.
     * @return ApiResponse containing the response from the API.
     */

    suspend fun evaluateExpression(expressions: List<String>): ApiResponse {
        // Create an ExpressionRequest object from the list of expressions
        val expressionRequest = ExpressionRequest(expressions)

        // Log the expressions being evaluated (for debugging)
//        Log.e("Demo", "evaluateExpression API call for expressions: $expressions")

        // Make the API call to evaluate expressions and return the ApiResponse
        return withContext(Dispatchers.IO) {
            apiService.evaluateExpression(expressionRequest)
        }
    }

    /**
     * Save a history item to the local database.
     *
     * @param item HistoryItem object to be saved.
     */

    suspend fun saveHistoryItem(item: HistoryItem) {
        // Log the item being saved (for debugging)
//        Log.e("repo", "saveHistoryItem: $item")

        // Insert the history item into the database
        withContext(Dispatchers.IO) {
            historyDao.insertHistoryItem(item)
        }
    }

    /**
     * Get all history items from the local database.
     *
     * @return List of HistoryItem objects.
     */

    suspend fun getAllHistoryItems(): List<HistoryItem> {
        // Fetch all history items from the database
        return withContext(Dispatchers.IO) {
            historyDao.getAllHistoryItems()
        }
    }
}