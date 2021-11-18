package com.electrolux.findphoto

import android.util.Log
import com.electrolux.findphoto.cache.Database
import com.electrolux.findphoto.cache.DatabaseDriverFactory
import com.electrolux.findphoto.entity.PicturesInfoDAO

import com.electrolux.findphoto.network.FlickrApi

class PicturesSDK (databaseDriverFactory: DatabaseDriverFactory) {
    companion object {
        private const val TOTAL_PHOTO_COUNT = 21
    }
    private val database = Database(databaseDriverFactory)
    private val api = FlickrApi()

    @Throws(Exception::class) suspend fun getAllPicturesInfo(searchTag: String, forceReload: Boolean): PicturesInfoDAO {
        val cachedLaunches = database.getAllPicturesInfo()
        return if (cachedLaunches != null
            && !forceReload
            && cachedLaunches.searchTag == searchTag) {
            cachedLaunches
        } else {
            api.getAllPictures(searchTag, TOTAL_PHOTO_COUNT).toDAO().also {
                database.clearDatabase()
                database.storePictures(it)
            }
        }
    }
}
