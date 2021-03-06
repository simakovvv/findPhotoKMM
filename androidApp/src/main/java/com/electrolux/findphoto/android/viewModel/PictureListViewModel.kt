package com.electrolux.findphoto.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.entity.PictureDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

sealed class PictureListIntent {
    data class LoadList(val key: String) : PictureListIntent()
    object LoadCashed : PictureListIntent()
    data class SelectPicture(val pictureId: Int) : PictureListIntent()
    object DownloadSelectedPictures : PictureListIntent()
    data class Download(val pictureId: Int) : PictureListIntent()
}

data class PictureListState(
    val loading: Boolean = false,
    val searchTag: String = "",
    val list: List<PictureDetails> = listOf(),
    val error: Exception? = null
)

class PictureListViewModel(
    private val sdk: PicturesSDK
) : ViewModel() {
    private val uiIntent = Channel<PictureListIntent>(Channel.UNLIMITED)

    private val _listState = MutableStateFlow(PictureListState(false, "", listOf(), null))
    val listState = _listState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // handle thrown exceptions from coroutine scope
            Log.e("PictureListViewModel coroutineExceptionHandler", throwable.toString())
            if (throwable is java.lang.Exception) {
                viewModelScope.launch {
                    _listState.emit(listState.value.copy(error = throwable))
                }
            }
        }

    init {
        handleIntent()
        sendIntent(PictureListIntent.LoadCashed)
    }

    fun sendIntent(intent: PictureListIntent) = viewModelScope.launch(Dispatchers.IO) {
        uiIntent.send(intent)
    }

    private fun handleIntent() = viewModelScope.launch(coroutineExceptionHandler) {
        withContext(Dispatchers.IO) {
            uiIntent.consumeAsFlow().collect {
                when (it) {
                    is PictureListIntent.Download -> {}//TODO()
                    PictureListIntent.DownloadSelectedPictures -> {}//TODO()
                    is PictureListIntent.LoadCashed -> {
                        val cashedData = sdk.getCachedData()
                        if (cashedData != null) {
                            _listState.emit(
                                listState.value.copy(
                                    searchTag = cashedData.searchTag,
                                    list = cashedData.picturesList,
                                    loading = false
                                )
                            )
                        } else {
                            sendIntent(PictureListIntent.LoadList("Electrolux"))
                        }
                    }
                    is PictureListIntent.LoadList -> {
                        _listState.emit(listState.value.copy(loading = true))
                        val picturesInfo = sdk.getAllPicturesInfo(searchTag = it.key).picturesList
                        _listState.emit(
                            listState.value.copy(
                                searchTag = it.key,
                                list = picturesInfo,
                                loading = false
                            )
                        )
                    }
                    is PictureListIntent.SelectPicture -> {}//TODO()
                }
            }
        }
    }
}