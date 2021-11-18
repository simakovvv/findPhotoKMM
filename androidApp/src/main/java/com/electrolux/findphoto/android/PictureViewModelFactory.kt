package com.electrolux.findphoto.android

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.cache.DatabaseDriverFactory

internal class PictureViewModelFactory(private val sdk: PicturesSDK) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PictureViewModel::class.java)) {
            return PictureViewModel(sdk) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}

internal fun providePictureViewModelFactory(context: Context) = PictureViewModelFactory(
    PicturesSDK(DatabaseDriverFactory(context))
).create(PictureViewModel::class.java)