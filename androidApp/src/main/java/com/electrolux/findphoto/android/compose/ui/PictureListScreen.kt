package com.electrolux.findphoto.android.compose.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.electrolux.findphoto.android.MainActivity.Companion.DETAILS_DESTINATION
import com.electrolux.findphoto.android.PictureListIntent
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
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()

    ) {
        SearchView(textState, viewModel)
        if(listState.loading) {
            LinearProgressIndicator( modifier = Modifier.fillMaxWidth())
        }

        if(listState.error != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize() ){
                Text(stringResource(com.electrolux.findphoto.android.R.string.error_explanation))
            }
        } else {
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
    }
}

@Composable
private fun SearchView(
    state: MutableState<TextFieldValue>,
    viewModel: PictureListViewModel
) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            viewModel.sendIntent(PictureListIntent.LoadList(value.text))
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
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
            .clickable(enabled = true, onClick = { navController.navigate("$DETAILS_DESTINATION/${details.id}") }),
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