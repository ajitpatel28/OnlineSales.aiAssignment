package com.ajit.onlinesalesaiassignment.data.api

import com.ajit.onlinesalesaiassignment.data.model.ApiResponse
import com.ajit.onlinesalesaiassignment.data.model.ExpressionRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ExpressionApiService {
    @POST(" ")
    suspend fun evaluateExpression(@Body request: ExpressionRequest): ApiResponse


}
