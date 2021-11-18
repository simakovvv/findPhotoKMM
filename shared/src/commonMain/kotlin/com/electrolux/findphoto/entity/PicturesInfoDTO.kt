package com.electrolux.findphoto.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.random.Random

data class PicturesInfoDTO(
    var searchTag: String,
    val result: FlickrPicturesResult
) {
    fun toDAO(): PicturesInfoDAO = PicturesInfoDAO(
        searchTag,
        result.photos.photo
            .filter { it.url_m != null }
            .map {
                PictureDetails(it.id.toIntOrNull()?: Random.nextInt(), it.title, it.url_m!!)
            })
}

@Serializable
data class FlickrPicturesResult(
    val photos: Photos,
    val stat: String
)

@Serializable
data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<Photo>
)

@Serializable
data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: Int,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    val url_m: String? = null,
    val height_m: Int? = null,
    val width_m: Int? = null
)