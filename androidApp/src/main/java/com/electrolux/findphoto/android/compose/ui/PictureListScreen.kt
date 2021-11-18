package com.electrolux.findphoto.android.compose.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.electrolux.findphoto.android.PictureListViewModel
import com.electrolux.findphoto.android.viewModel.Injector
import com.electrolux.findphoto.entity.PictureDetails

@Composable
internal fun PictureList(
    context: Context,
    navController: NavHostController,
    viewModel: PictureListViewModel =
        viewModel(factory = Injector.providePictureListViewModelFactory(context))

) {
    val listState by viewModel.listState.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = listState.list,
            itemContent = {
                PictureDetailsItem(it, listState.searchTag, listState.list.indexOf(it), navController)
            })
    }
}


@Composable
internal fun PictureDetailsItem(
    details: PictureDetails,
    searchTag: String,
    num: Int,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(enabled = true, onClick = { navController.navigate("pictureDetails/${details.id}") }),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Picture(details.url)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = details.title, style = MaterialTheme.typography.h6)
                Text(
                    text = "Num: $num. Search tag: $searchTag",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
            }
        }
    }

}

@Composable
internal fun Picture(url: String) {
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
                .padding(8.dp)
                .size(84.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
        )
    }
}