package com.abdulaziz.a4kfullwallpapers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.Result
import com.abdulaziz.a4kfullwallpapers.retrofit.ApiClient
import com.abdulaziz.a4kfullwallpapers.util.ImageData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AllViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<Result>>()
    private val client_id = "tir1o8IXotsiLUzt7dNH82uQg9PtNYO904K-xDm52dg"
    var page = 1

    fun getAllImages() : LiveData<List<Result>> {

        val pagesList = ImageData.getPagesList()

        val list: ArrayList<Result> = arrayListOf()
        val apiService = ApiClient.apiServiceSearch
        viewModelScope.launch{
            pagesList.forEach {
                val async = async { apiService.getImages(client_id, page.toString(), it) }
                val await = async.await()
                await.results.forEach {
                    list.add(it)
                }
            }
            list.shuffle()
            liveData.postValue(list)
        }
        return liveData
    }

}