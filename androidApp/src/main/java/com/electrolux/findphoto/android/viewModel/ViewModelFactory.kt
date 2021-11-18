package com.electrolux.findphoto.android.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.android.PictureDetailsViewModel
import com.electrolux.findphoto.android.PictureListViewModel

internal class PictureListViewModelFactory(private val sdk: PicturesSDK) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PictureListViewModel::class.java)) {
            return PictureListViewModel(sdk) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}

internal class PictureDetailsViewModelFactory(private val sdk: PicturesSDK) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PictureDetailsViewModel::class.java)) {
            return PictureDetailsViewModel(sdk) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}