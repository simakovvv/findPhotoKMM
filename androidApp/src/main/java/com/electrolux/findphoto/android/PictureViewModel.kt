package com.electrolux.findphoto.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.entity.Picture
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

sealed class PictureListIntent {
    data class LoadList(val key: String) : PictureListIntent()
    data class RefreshList(val forceLoad: Boolean) : PictureListIntent()
    data class SelectPicture(val picture: Picture) : PictureListIntent()
    object DownloadSelectedPictures : PictureListIntent()
    data class Download(val pictureId: Int) : PictureListIntent()
}

sealed class PictureListState {
    object Loading : PictureListState()
    data class UpdateItem(val picture: Picture) : PictureListState()
    data class UpdateList(val list: List<Picture>) : PictureListState()
    data class Error(val error: Exception) : PictureListState()
}

class PictureViewModel(
    private val sdk: PicturesSDK
) : ViewModel() {
    private val uiIntent = Channel<PictureListIntent>(Channel.UNLIMITED)

    private val _uiState = MutableStateFlow(PictureListState.Loading)
    val uiState: StateFlow<PictureListState> = _uiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // handle thrown exceptions from coroutine scope
        Log.e("Flickr coroutineExceptionHandler", throwable.toString())
        throwable.printStackTrace()
    }
    init {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        handleIntent()
    }

    fun sendIntent(intent: PictureListIntent) = viewModelScope.launch(Dispatchers.IO)  {
        uiIntent.send(intent)
    }

    private fun handleIntent() = viewModelScope.launch(coroutineExceptionHandler) {
        withContext(Dispatchers.IO) {
            uiIntent.consumeAsFlow().collect {
                when (it) {
                    is PictureListIntent.Download -> {}//TODO()
                    PictureListIntent.DownloadSelectedPictures -> {}//TODO()
                    is PictureListIntent.LoadList -> {
                        val test = sdk.getAllPicturesInfo(it.key, true)
                        Log.d("Flickr",test.picturesList.size.toString())
                    }//TODO()
                    is PictureListIntent.RefreshList -> {}//TODO()
                    is PictureListIntent.SelectPicture -> {}//TODO()
                }
            }
        }
    }
}