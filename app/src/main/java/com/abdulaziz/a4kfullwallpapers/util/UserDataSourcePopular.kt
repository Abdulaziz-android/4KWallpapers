package com.abdulaziz.a4kfullwallpapers.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdulaziz.a4kfullwallpapers.models.newmodel.NewImageModelItem
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiService

class UserDataSourcePopular(val apiService: ApiService, val page:String) : PagingSource<Int, NewImageModelItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewImageModelItem>): Int? {
        return state.anchorPosition
    }

    val client_id = "tir1o8IXotsiLUzt7dNH82uQg9PtNYO904K-xDm52dg"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewImageModelItem> {
        try {
            val nextPageNumber = params.key ?: 1
            val imageData = apiService.getPopularImages(client_id, nextPageNumber.toString(), "popular")
            return LoadResult.Page(imageData, null, nextPageNumber+1)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}