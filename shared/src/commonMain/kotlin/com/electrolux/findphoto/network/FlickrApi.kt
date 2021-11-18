package com.electrolux.findphoto.network

import com.electrolux.findphoto.entity.FlickrPicturesResult
import com.electrolux.findphoto.entity.PicturesInfoDTO
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class FlickrApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getAllPicturesInfo(keyWord: String, totalCount: Int): PicturesInfoDTO  {
        val result: FlickrPicturesResult =  httpClient.get(LAUNCHES_ENDPOINT) {
            parameter("api_key", API_KEY)
            parameter("method", SERVICE_NAME)
            parameter("tags", keyWord)
            parameter("format", "json")
            parameter("nojsoncallback", "true")
            parameter("extras", "media")
            parameter("extras", "url_sq")
            parameter("extras", "url_m")
            parameter("per_page", totalCount)
            parameter("page", 1)
        }

        return PicturesInfoDTO(keyWord, result)
    }

    companion object {
        private const val API_KEY = "41f3be7ccc3684f31f3cb667a3fc45de"
        private const val SERVICE_NAME = "flickr.photos.search"
        private const val LAUNCHES_ENDPOINT = "https://api.flickr.com/services/rest"
    }
}