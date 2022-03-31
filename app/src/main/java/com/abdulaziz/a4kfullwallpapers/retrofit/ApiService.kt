package com.abdulaziz.a4kfullwallpapers.retrofit

import com.abdulaziz.a4kfullwallpapers.models.newmodel.NewImageModel
import com.abdulaziz.a4kfullwallpapers.models.randommodel.RandomImageModel
import com.abdulaziz.a4kfullwallpapers.models.searchmodel.SearchImageModel
import retrofit2.http.GET
import retrofit2.http.Query

interface   ApiService {

    @GET("photos")
    suspend fun getImages(
        @Query("client_id") client_id: String,
        @Query("page") page: String,
        @Query("query") query: String,
    ): SearchImageModel

    @GET("photos")
    suspend fun getNewImages(
        @Query("client_id") client_id: String,
        @Query("page") page: String,
    ): NewImageModel

    @GET("random")
    suspend fun getRandomImage(
        @Query("client_id") client_id: String,
        @Query("page") page: String,
        @Query("count") count: String,
    ): RandomImageModel

    @GET("photos")
    suspend fun getPopularImages(
        @Query("client_id") client_id: String,
        @Query("page") page: String,
        @Query("order_by") order_by: String,
    ): NewImageModel

}