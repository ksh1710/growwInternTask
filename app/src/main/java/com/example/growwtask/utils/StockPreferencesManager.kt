package com.example.growwtask.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.growwtask.data.model.RecentlySearchedStock
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "stock_preferences")

    private val gson = Gson()

    companion object {
        private val RECENTLY_SEARCHED_KEY = stringPreferencesKey("recently_searched")
        private const val MAX_RECENT_SEARCHES = 20 // Max to store
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun saveRecentSearch(stock: RecentlySearchedStock) {
        context.dataStore.edit { preferences ->
            val recentSearchesJson = preferences[RECENTLY_SEARCHED_KEY] ?: "[]"
            val recentSearches = try {
                gson.fromJson(recentSearchesJson, Array<RecentlySearchedStock>::class.java).toMutableList()
            } catch (e: Exception) {
                Log.e("idk", "saveRecentSearch: $e ")
                mutableListOf()
            }

            recentSearches.removeIf{ it.symbol == stock.symbol }

            recentSearches.add(0, stock)

            if (recentSearches.size > MAX_RECENT_SEARCHES) {
                recentSearches.removeAt(recentSearches.size - 1)
            }

            preferences[RECENTLY_SEARCHED_KEY] = gson.toJson(recentSearches)
        }
    }

    fun getRecentSearches(): Flow<List<RecentlySearchedStock>> {
        return context.dataStore.data.map { preferences ->
            val recentSearchesJson = preferences[RECENTLY_SEARCHED_KEY] ?: "[]"
            try {
                gson.fromJson(recentSearchesJson, Array<RecentlySearchedStock>::class.java).toList()
            } catch (e: Exception) {
                Log.e("idk", "getRecentSearches: $e ")
                emptyList()
            }
        }
    }

    suspend fun clearRecentSearches() {
        context.dataStore.edit { preferences ->
            preferences[RECENTLY_SEARCHED_KEY] = "[]"
        }
    }
}