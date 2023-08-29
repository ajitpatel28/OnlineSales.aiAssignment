package com.ajit.onlinesalesaiassignment.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ajit.onlinesalesaiassignment.utils.DummyHistoryItem
import com.ajit.onlinesalesaiassignment.data.model.ApiResponse
import com.ajit.onlinesalesaiassignment.data.repository.ExpressionRepository
import com.ajit.onlinesalesaiassignment.utils.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ExpressionViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExpressionViewModel
    private lateinit var repository: ExpressionRepository
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        repository = mockk()
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getAllHistoryItems() } coAnswers {
            listOf(DummyHistoryItem.getDummyHistoryItems().first())
        }
        viewModel = ExpressionViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when loadHistoryItems is called, then historyItems is populated`() =
        testDispatcher.runBlockingTest {
            val historyItems = DummyHistoryItem.getDummyHistoryItems()
            coEvery { repository.getAllHistoryItems() } returns historyItems

            viewModel.loadHistoryItems()

            delay(10) // Wait for the coroutine to finish

            assertEquals(historyItems, viewModel.historyItems.value)
        }


    @Test
    fun `when evaluateExpression returns result, then expressionResult is Success`() =
        testDispatcher.runBlockingTest {
            val response = ApiResponse(
                result = listOf("4"),
                error = null
            )
            coEvery { repository.evaluateExpression(any()) } returns response

            viewModel.evaluateExpression("2 + 2")

            delay(10) // Wait for the coroutine to finish

            assertTrue(viewModel.expressionResult.value is UiState.Success)
        }


    @Test
    fun `when submitExpressionBuffer is called, then expressionResult is Success`() =
        testDispatcher.runBlockingTest {
            val response = ApiResponse(
                result = listOf("4"),
                error = null
            )
            coEvery { repository.evaluateExpression(any()) } returns response
            coEvery { repository.saveHistoryItem(any()) } returns Unit
            val item = DummyHistoryItem.getDummyHistoryItems().first()
            coEvery { repository.getAllHistoryItems() } returns listOf(item)

            viewModel.evaluateExpression("2 + 2")

            // Trigger the condition by setting expression buffer size to 501
            repeat(501) {
                viewModel.evaluateExpression("$it")
            }

            delay(10) // Wait for the coroutine to finish

            assertTrue(viewModel.expressionResult.value is UiState.Success)
        }


    @Test
    fun `when submitExpressionBuffer encounters an error, then expressionResult is Error`() =
        testDispatcher.runBlockingTest {
            val errorMessage = "Test error"
            coEvery { repository.evaluateExpression(any()) } throws Exception(errorMessage)

            viewModel.evaluateExpression("wrong_expr")

            // Trigger the condition by setting expression buffer size to 501
            repeat(501) {
                viewModel.evaluateExpression("$it")
            }

            delay(10) // Wait for the coroutine to finish

            assertTrue(viewModel.expressionResult.value is UiState.Error)
            assertEquals(errorMessage, (viewModel.expressionResult.value as UiState.Error).message)
        }

    @Test
    fun `when submitExpressionBuffer is called with insufficient expressions, evaluateExpression is not called`() =
        testDispatcher.runBlockingTest {
            viewModel.evaluateExpression("2 + 2")
            delay(900) // Not enough time for submission

            // Assert that evaluateExpression is not called
            coVerify(exactly = 0) { repository.evaluateExpression(any()) }
            assertEquals(listOf("2 + 2"), viewModel.expressionBuffer)
        }

    @Test
    fun `when saveHistoryItem is called, then repository is called and loadHistoryItems is called`() =
        testDispatcher.runBlockingTest {
            val item = DummyHistoryItem.getDummyHistoryItems().first()
            coEvery { repository.saveHistoryItem(any()) } returns Unit
            coEvery { repository.getAllHistoryItems() } returns listOf(item)

            viewModel.saveHistoryItem(item)

            // Verify that repository methods are called and historyItems is updated
            coVerify { repository.saveHistoryItem(item) }
            coVerify { repository.getAllHistoryItems() }
            assertEquals(listOf(item), viewModel.historyItems.value)
        }


}