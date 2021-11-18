package com.electrolux.findphoto.entity
import kotlinx.serialization.Serializable
import android.graphics.Bitmap
import kotlinx.serialization.SerialName

@Serializable
data class PicturesInfoDAO(
    @SerialName("picture_tag")
    var searchTag : String,
    var picturesList : List<PictureDetails>,
)

@Serializable
data class PictureDetails(
    @SerialName("picture_id")
    val id: Int,
    @SerialName("picture_title")
    val title: String,
    @SerialName("picture_url")
    val url: String
)


data class Picture (
    val path: PicturesInfoDAO,
    val bitmap: Bitmap?
)