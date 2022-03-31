package com.abdulaziz.a4kfullwallpapers.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageModel(
    @PrimaryKey
    val id:String,
    val width:Int,
    val height:Int,
    val url_small:String,
    val url_regular:String,
    val link_download:String,
    val popular_like:Int,
    val website:String,
    val author:String,
    var like:Boolean
)
