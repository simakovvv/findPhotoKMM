package com.electrolux.findphoto.cache

import com.electrolux.findphoto.entity.PictureDetails
import com.electrolux.findphoto.entity.PicturesInfoDAO


internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = PicturesDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.picturesDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllPicturesInfo()
        }
    }

    internal fun getAllPicturesInfo(): PicturesInfoDAO? {
        val picList = dbQuery.selectAllPicturesInfo(::mapPicturesSelecting).executeAsList()
        return if(picList.isNotEmpty()) {
            PicturesInfoDAO(dbQuery.getSearchTag().executeAsOne(), picList)
        } else null
    }

    private fun mapPicturesSelecting(
        picture_id: Long,
        picture_tag: String,
        picture_title: String,
        picture_url: String
    ): PictureDetails {
        return PictureDetails(
            id = picture_id.toInt(),
            title = picture_title,
            url = picture_url
        )
    }

    internal fun storePictures(pictures: PicturesInfoDAO) {
        dbQuery.transaction {
            pictures.picturesList.forEach { picture ->
                insertPictureInfo(pictures.searchTag, picture)
            }
        }
    }

    private fun insertPictureInfo(searchTag: String, info: PictureDetails) {
        dbQuery.insertPictureInfo(
            picture_id = info.id.toLong(),
            picture_tag = searchTag,
            picture_title = info.title,
            picture_url = info.url
        )
    }
}