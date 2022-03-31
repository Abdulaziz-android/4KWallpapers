package com.abdulaziz.a4kfullwallpapers.database

import androidx.room.*
import com.abdulaziz.a4kfullwallpapers.models.ImageModel

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(imageModel: ImageModel)

    @Query("select * from imagemodel")
    fun getAllImages():List<ImageModel>

    @Query("select * from imagemodel where `like` = 1")
    fun getLikeImages():List<ImageModel>

    @Query("select * from imagemodel where id = :id")
    fun getImageById(id:String):ImageModel?

    @Delete
    fun deleteImage(imageModel: ImageModel)

    @Query("select exists (select * from imagemodel)")
    fun isExists():Boolean
}