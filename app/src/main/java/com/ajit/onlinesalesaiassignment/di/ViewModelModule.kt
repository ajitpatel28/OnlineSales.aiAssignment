package com.ajit.onlinesalesaiassignment.di

import com.ajit.onlinesalesaiassignment.data.repository.ExpressionRepository
import com.ajit.onlinesalesaiassignment.ui.viewmodel.ExpressionViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {


    @Provides
    @Singleton
    fun provideExpressionViewModel(repository: ExpressionRepository): ExpressionViewModel {
        return ExpressionViewModel(repository)
    }

}
