package com.electrolux.findphoto.android.compose.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.electrolux.findphoto.android.PictureDetailsIntent
import com.electrolux.findphoto.android.PictureDetailsState
import com.electrolux.findphoto.android.PictureDetailsViewModel
import com.electrolux.findphoto.android.viewModel.Injector
import com.electrolux.findphoto.entity.PictureDetails

@Composable
fun PictureDetailsScreen(
    context: Context,
    navController: NavHostController,
    pictureId: Int?,
    viewModel: PictureDetailsViewModel =
        viewModel(factory = Injector.providePictureDetailsViewModelFactory(context)),
) {
    pictureId?.let {
        viewModel.sendIntent(PictureDetailsIntent.Load(it))
    }

    val profileState by viewModel.profileState.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    PictureDetailsHeader(
                        profileState.item.url,
                        this@BoxWithConstraints.maxHeight
                    )
                    ProfileContent(profileState,this@BoxWithConstraints.maxHeight)
                }
            }
        }
    }
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        onClick = { navController.navigate("pictureList") }) {
        Text("Go back to list!")
    }
}

@Composable
private fun PictureDetailsHeader(
    url: String,
    containerHeight: Dp
) {
    val imageLoader = LocalImageLoader.current
    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        val painter = rememberImagePainter(
            data = url,
            builder = {
                crossfade(true)
                memoryCachePolicy(policy = CachePolicy.ENABLED)
            }
        )
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun ProfileContent(picDetails: PictureDetailsState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        picDetails.item?.title?.let {
            Title(it)
        }

        Title("Num: ${picDetails.number}. Search tag: ${picDetails.searchTag}")
        ProfileProperty("Num:", picDetails.number.toString())
        ProfileProperty("Search tag:", picDetails.searchTag)
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun Title(
    title: String
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider(modifier = Modifier.padding(bottom = 4.dp))
        Text(
            text = label,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = value,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Visible
        )
    }
}
