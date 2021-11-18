package com.electrolux.findphoto

import com.electrolux.findphoto.cache.Database
import com.electrolux.findphoto.cache.DatabaseDriverFactory
import com.electrolux.findphoto.entity.PicturesInfoDAO

import com.electrolux.findphoto.network.FlickrApi

class PicturesSDK(databaseDriverFactory: DatabaseDriverFactory) {
    companion object {
        private const val TOTAL_PHOTO_COUNT = 21
    }

    private val database = Database(databaseDriverFactory)
    private val api = FlickrApi()

    // returns stored search results
    suspend fun getCachedData() = database.getAllPicturesInfo()

    // returns search results by tag
    @Throws(Exception::class)
    suspend fun getAllPicturesInfo(
        searchTag: String,
        forceReload: Boolean = false
    ): PicturesInfoDAO {
        val cached = database.getAllPicturesInfo()
        return if (cached != null
            && !forceReload
            && cached.searchTag == searchTag
        ) {
            cached
        } else {
            api.getAllPicturesInfo(searchTag, TOTAL_PHOTO_COUNT).toDAO().also {
                database.clearDatabase()
                database.storePictures(it)
            }
        }
    }
}
