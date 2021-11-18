package com.electrolux.findphoto.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.entity.Picture
import com.electrolux.findphoto.entity.PictureDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

sealed class PictureListIntent {
    data class LoadList(val key: String) : PictureListIntent()
    data class SelectPicture(val picture: Picture) : PictureListIntent()
    object DownloadSelectedPictures : PictureListIntent()
    data class Download(val pictureId: Int) : PictureListIntent()
}

data class PictureListState(
    val loading: Boolean = false,
    val searchTag: String = "",
    val list: List<PictureDetails> = listOf(),
    val error: Exception? = null
)

class PictureViewModel(
    private val sdk: PicturesSDK
) : ViewModel() {
    private val uiIntent = Channel<PictureListIntent>(Channel.UNLIMITED)

    private val _uiState = MutableStateFlow(PictureListState(false, "", listOf(), null))
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // handle thrown exceptions from coroutine scope
        Log.e("Flickr coroutineExceptionHandler", throwable.toString())
        if(throwable is java.lang.Exception) {
            viewModelScope.launch {
                _uiState.emit(uiState.value.copy(error = throwable))
            }
        }
    }
    init {
        handleIntent()
        sendIntent(PictureListIntent.LoadList("Electrolux"))
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
                        val picturesInfo = sdk.getAllPicturesInfo( searchTag = it.key).picturesList
                        _uiState.emit(uiState.value.copy(searchTag = it.key, list = picturesInfo))
                    }
                    is PictureListIntent.SelectPicture -> {}//TODO()
                }
            }
        }
    }
}