package com.example.growwtask.di

import com.example.growwtask.utils.StockDataCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    
    @Provides
    @Singleton
    fun provideStockDataCache(): StockDataCache {
        return StockDataCache()
    }
}