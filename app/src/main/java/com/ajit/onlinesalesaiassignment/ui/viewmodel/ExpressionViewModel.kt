package com.ajit.onlinesalesaiassignment.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajit.onlinesalesaiassignment.data.model.HistoryItem
import com.ajit.onlinesalesaiassignment.data.repository.ExpressionRepository
import com.ajit.onlinesalesaiassignment.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpressionViewModel @Inject constructor(
    private val repository: ExpressionRepository
) : ViewModel() {

    private val MAX_EXPRESSIONS_PER_REQUEST = 50



    // Mutable state for expression result
    private val _expressionResult = mutableStateOf<UiState<String>>(UiState.Success(""))
    val expressionResult: MutableState<UiState<String>> = _expressionResult

    // Mutable state for history items
    private val _historyItems = mutableStateOf<List<HistoryItem>>(emptyList())
    val historyItems: MutableState<List<HistoryItem>> = _historyItems


    //List to store formatted Results
    private val formattedResults = mutableListOf<String>()


    val expressionBuffer = mutableListOf<String>()
    private var lastSubmittedTime = System.currentTimeMillis()

    // Initialize ViewModel by loading history items
    init {
        loadHistoryItems()
    }



    // Function to evaluate expression
    fun evaluateExpression(expression: String) {
        val expressions = expression.split("\n").map { it.trim() }

        expressionBuffer.addAll(expressions)
        val currentTime = System.currentTimeMillis()
        if (expressionBuffer.size >= 500 || (currentTime - lastSubmittedTime) >= 1000) {
            while (expressionBuffer.isNotEmpty()) {
                val expressionsToSubmit = expressionBuffer.take(MAX_EXPRESSIONS_PER_REQUEST)
                submitExpressionBuffer(expressionsToSubmit.toList())
                expressionBuffer.removeAll(expressionsToSubmit)
            }
            lastSubmittedTime = currentTime
        }
        formattedResults.clear()

    }

    private fun submitExpressionBuffer(expressions:List<String>) {
        viewModelScope.launch {
            try {
                _expressionResult.value = UiState.Loading() // Set loading state



                // Call repository to evaluate expressions

                val response = repository.evaluateExpression(expressions)
                val results = response.result as? List<Any> ?: emptyList<Any>()


                // Process results and format expressions and results
                for ((i, expr) in expressions.withIndex()) {
                    val result = results.getOrNull(i)?.toString() ?: "N/A"
                    formattedResults.add("$expr => $result")
                    saveHistoryItem(HistoryItem(expression = expr, result = result))
                }

                // Update expression result with formatted results
                _expressionResult.value = UiState.Success(formattedResults.joinToString(", "))

            } catch (e: Exception) {
                // Handle error and update expression result with error message
                _expressionResult.value = e.message?.let { UiState.Error(it) }!!
            }
        }
    }

    // Function to save history item
    fun saveHistoryItem(item: HistoryItem) {
        viewModelScope.launch {
            repository.saveHistoryItem(item)
            loadHistoryItems()
        }
    }

    // Function to load history items
     fun loadHistoryItems() {
        viewModelScope.launch {
            val items = repository.getAllHistoryItems()
            _historyItems.value = items
        }
    }
}