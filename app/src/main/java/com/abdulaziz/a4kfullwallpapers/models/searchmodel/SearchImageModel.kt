package com.abdulaziz.a4kfullwallpapers.models.searchmodel

data class SearchImageModel(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)