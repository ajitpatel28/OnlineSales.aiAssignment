package com.ajit.onlinesalesaiassignment.di


import com.ajit.onlinesalesaiassignment.data.api.ExpressionApiService
import com.ajit.onlinesalesaiassignment.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideExpressionApiService(moshi: Moshi): ExpressionApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Adjust the base URL accordingly
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ExpressionApiService::class.java)
    }


}
