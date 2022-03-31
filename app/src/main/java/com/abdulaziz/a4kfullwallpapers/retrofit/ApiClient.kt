package com.abdulaziz.a4kfullwallpapers.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val BASE_URL_SEARCH = "https://api.unsplash.com/search/"
    val BASE_URL_NEW = "https://api.unsplash.com/"
    val BASE_URL_RANDOM = "https://api.unsplash.com/photos/"
    val BASE_URL_POPULAR = "https://api.unsplash.com/"

    fun getRetrofit(base_url : String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiServiceSearch = getRetrofit(BASE_URL_SEARCH).create(ApiService::class.java)
    val apiServiceNew = getRetrofit(BASE_URL_NEW).create(ApiService::class.java)
    val apiServiceRandom = getRetrofit(BASE_URL_RANDOM).create(ApiService::class.java)
    val apiServicePopular = getRetrofit(BASE_URL_POPULAR).create(ApiService::class.java)
}