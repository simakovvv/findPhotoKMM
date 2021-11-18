package com.electrolux.findphoto.entity
import kotlinx.serialization.Serializable
import android.graphics.Bitmap
import kotlinx.serialization.SerialName


data class PicturesInfoDAO(
    var searchTag : String,
    var picturesList : List<PictureDetails>,
)

@Serializable
data class PictureDetails(
    val id: Int,
    val title: String,
    val url: String
)