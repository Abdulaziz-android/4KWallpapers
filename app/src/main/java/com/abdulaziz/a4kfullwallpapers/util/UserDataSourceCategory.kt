package com.abdulaziz.a4kfullwallpapers.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.Result
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiService

class UserDataSourceCategory(val apiService: ApiService, val page:String) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    val client_id = "tir1o8IXotsiLUzt7dNH82uQg9PtNYO904K-xDm52dg"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        try {
            val nextPageNumber = params.key ?: 1
            val imageData = apiService.getImages(client_id, nextPageNumber.toString(), page)
            return LoadResult.Page(imageData.results, null, nextPageNumber+1)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}