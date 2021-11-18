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
    @SerialName("photos")
    val photos: Photos,
    @SerialName("stat")
    val stat: String
)

@Serializable
data class Photos(
    @SerialName("page")
    val page: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("perpage")
    val perpage: Int,
    @SerialName("total")
    val total: Int,
    @SerialName("photo")
    val photo: List<Photo>
)

@Serializable
data class Photo(
    @SerialName("id")
    val id: String,
    @SerialName("owner")
    val owner: String,
    @SerialName("secret")
    val secret: String,
    @SerialName("server")
    val server: Int,
    @SerialName("farm")
    val farm: Int,
    @SerialName("title")
    val title: String,
    @SerialName("ispublic")
    val ispublic: Int,
    @SerialName("isfriend")
    val isfriend: Int,
    @SerialName("isfamily")
    val isfamily: Int,
    @SerialName("url_m")
    val url_m: String? = null,
    @SerialName("height_m")
    val height_m: Int? = null,
    @SerialName("width_m")
    val width_m: Int? = null
)