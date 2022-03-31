package com.abdulaziz.a4kfullwallpapers.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdulaziz.a4kfullwallpapers.models.randommodel.RandomImageModelItem
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiService

class UserDataSourceRandom(val apiService: ApiService, val page: String) :
    PagingSource<Int, RandomImageModelItem>() {
    override fun getRefreshKey(state: PagingState<Int, RandomImageModelItem>): Int? {
        return state.anchorPosition
    }

    val client_id = "tir1o8IXotsiLUzt7dNH82uQg9PtNYO904K-xDm52dg"


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RandomImageModelItem> {
        try {
            val nextPageNumber = params.key ?: 0
            val imageData = apiService.getRandomImage(client_id, nextPageNumber.toString(),"10")
            return LoadResult.Page(imageData, null, nextPageNumber + 1)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}