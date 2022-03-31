package com.abdulaziz.a4kfullwallpapers.models

import android.graphics.Bitmap
import com.zomato.photofilters.imageprocessors.Filter

data class Effect(
    val filter:Filter,
    val bitmap: Bitmap,
    val name:String
)
