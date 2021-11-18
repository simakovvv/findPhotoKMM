package com.electrolux.findphoto.android.viewModel

import android.content.Context
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.cache.DatabaseDriverFactory

object Injector {
    internal fun providePictureListViewModelFactory(context: Context) = PictureListViewModelFactory(
        PicturesSDK(DatabaseDriverFactory(context))
    )

    internal fun providePictureDetailsViewModelFactory(pictureId: Int?,context: Context) = PictureDetailsViewModelFactory(
        pictureId,
        PicturesSDK(DatabaseDriverFactory(context))
    )
}