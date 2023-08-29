package com.ajit.onlinesalesaiassignment.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ajit.onlinesalesaiassignment.R
import com.ajit.onlinesalesaiassignment.ui.navigation.Destinations
import com.ajit.onlinesalesaiassignment.ui.viewmodel.ExpressionViewModel


@Composable
fun HistoryTab(viewModel: ExpressionViewModel,
               navController: NavHostController
) {
    val historyItems = viewModel.historyItems.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /**
        Button to navigate to history screen
         */

        IconButton(
            onClick = {
                navController.navigate(Destinations.MainScreen.route)
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display history items here using the historyItems list
        if(historyItems.isNotEmpty()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ){items(historyItems) { item ->
                    Text("${item.expression} => ${item.result}")
                }
            }
    }else{
            Text(
                text = stringResource(R.string.no_history),
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

    }


    }
}


