package com.ajit.onlinesalesaiassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ajit.onlinesalesaiassignment.ui.navigation.Destinations
import com.ajit.onlinesalesaiassignment.ui.viewmodel.ExpressionViewModel
import com.ajit.onlinesalesaiassignment.utils.UiState

@Composable
fun MainScreen(
    viewModel: ExpressionViewModel,
    navController: NavHostController
) {
    /**
    Mutable state for user input and error message
     */
    var expressionInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        /**
        Input field for expression
         */

        TextField(
            value = expressionInput,
            onValueChange = { input ->
                expressionInput = input
                errorMessage = null          // Clear error message when input changes
            },
            label = { Text("Enter Expression") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        )

        /**
        Display error message if it's not null
         */

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
        Button to submit expression
         */

        Button(
            onClick = {
                if (expressionInput.isNotEmpty()) {
                    viewModel.evaluateExpression(expressionInput)
                    expressionInput = ""        // Clear the input after evaluating
                } else {
                    errorMessage = "Please enter an expression to evaluate"
                }

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         Button to navigate to history screen
         */

        Button(
            onClick = {
                navController.navigate(Destinations.HistoryScreen.route)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Show History")
        }

        Spacer(modifier = Modifier.height(16.dp))


        /**
         Handle the different states of the expression result
         */

        when (val result = viewModel.expressionResult.value) {
            is UiState.Success -> {
                val formattedResult = result.data
                if (formattedResult != null) {
                    if (formattedResult.isNotEmpty()) {

                        Text(
                            text = "Results:",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(formattedResult.split(",")) { result ->
                                Text(
                                    text = result.trim(),
                                    color = Color.Black,
                                    modifier = Modifier
                                        .background(Color.Gray)
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
            is UiState.Loading -> {
                Text(
                    text = "Results:",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Display a progress indicator during loading
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiState.Error -> {
                // Display error message in case of error
                Text(
                    text = "Error: ${result.message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}