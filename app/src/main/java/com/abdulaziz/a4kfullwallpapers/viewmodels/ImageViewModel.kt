package com.abdulaziz.a4kfullwallpapers.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiClient
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiService
import com.abdulaziz.a4kfullwallpapers.util.UserDataSourceCategory
import com.abdulaziz.a4kfullwallpapers.util.UserDataSourceNew
import com.abdulaziz.a4kfullwallpapers.util.UserDataSourcePopular
import com.abdulaziz.a4kfullwallpapers.util.UserDataSourceRandom

class ImageViewModel : ViewModel() {

    var currentPage:String?=null
    var apiService:ApiService?=null

    val liveData = Pager(PagingConfig(pageSize = 2)){
        UserDataSourceCategory(ApiClient.apiServiceSearch, currentPage!!)
    }.flow.cachedIn(viewModelScope).asLiveData()

    val liveDataNew = Pager(PagingConfig(pageSize = 2)){
        UserDataSourceNew(ApiClient.apiServiceNew, currentPage!!)
    }.flow.cachedIn(viewModelScope).asLiveData()

    val liveDataRandom = Pager(PagingConfig(pageSize = 2)){
        UserDataSourceRandom(ApiClient.apiServiceRandom, currentPage!!)
    }.flow.cachedIn(viewModelScope).asLiveData()

    val liveDataPopular = Pager(PagingConfig(pageSize = 2)){
        UserDataSourcePopular(ApiClient.apiServicePopular, currentPage!!)
    }.flow.cachedIn(viewModelScope).asLiveData()

}