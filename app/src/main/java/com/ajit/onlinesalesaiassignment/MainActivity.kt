package com.ajit.onlinesalesaiassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ajit.onlinesalesaiassignment.ui.navigation.MainNavGraph
import com.ajit.onlinesalesaiassignment.ui.viewmodel.ExpressionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Create a NavController
            val viewModel: ExpressionViewModel = viewModel()

            MainNavGraph(navController, viewModel)
            }
        }
    }



