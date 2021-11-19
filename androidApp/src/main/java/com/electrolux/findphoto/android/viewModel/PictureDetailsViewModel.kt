package com.electrolux.findphoto.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.entity.PictureDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

sealed class PictureDetailsIntent {
    data class Load(val pictureId: Int) : PictureDetailsIntent()
}

data class PictureDetailsState(
    val loading: Boolean = false,
    val searchTag: String = "",
    val number: Int?,
    val item: PictureDetails? = null,
    val error: Exception? = null
)

data class PictureProfileState(
    val loading: Boolean = false,
    val searchTag: String = "",
    val number: Int,
    val item: PictureDetails?  = null
)

class PictureDetailsViewModel(
    pictureId: Int?,
    private val sdk: PicturesSDK
) : ViewModel() {
    private val uiIntent = Channel<PictureDetailsIntent>(Channel.UNLIMITED)

    private val _profileState = MutableStateFlow(PictureDetailsState(false, "", null, null))
    val profileState = _profileState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // handle thrown exceptions from coroutine scope
            Log.e("PictureDetailsState coroutineExceptionHandler", throwable.toString())
            if (throwable is java.lang.Exception) {
                viewModelScope.launch {
                    _profileState.emit(_profileState.value.copy(error = throwable))
                }
            }
        }

    init {
        handleIntent()
        pictureId?.let {
            sendIntent(PictureDetailsIntent.Load(pictureId))
        }

    }

    fun sendIntent(intent: PictureDetailsIntent) = viewModelScope.launch(Dispatchers.IO) {
        uiIntent.send(intent)
    }

    private fun handleIntent() = viewModelScope.launch(coroutineExceptionHandler) {
        withContext(Dispatchers.IO) {
            uiIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is PictureDetailsIntent.Load -> {
                        sdk.getCachedData()?.let { picsInfo ->
                            val selectedPic =
                                picsInfo.picturesList.find { it.id == intent.pictureId }
                            _profileState.emit(
                                _profileState.value.copy(
                                    searchTag = picsInfo.searchTag,
                                    number = picsInfo.picturesList.indexOf(selectedPic),
                                    item = selectedPic
                                )
                            )
                        }
                    }
                }
            }
        }
    }


}