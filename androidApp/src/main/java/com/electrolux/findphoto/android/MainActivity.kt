package com.electrolux.findphoto.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electrolux.findphoto.Greeting
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electrolux.findphoto.android.compose.ui.AppTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.ImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.electrolux.findphoto.entity.PictureDetails
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class MainActivity : AppCompatActivity() {
    companion object {
        const val LIST_DESTINATION = "pictureList"
        const val DETAILS_DESTINATION = "pictureDetails"
        const val DETAILS_ITEM_ID = "pictureId"

    }

    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = false,
                                applyEnd = true,
                                applyBottom = false
                            )
                        )
                    ) {
                        SetupNavigation()
                    }
                }
            }
        }
    }

    @Composable
    fun SetupNavigation() {
        navController = rememberNavController()
        NavHost(navController, startDestination = "pictureList") {
            composable(route = "pictureList") {
                PictureList()
            }
            composable(route = "pictureDetails") {
                PictureDetailsScreen()
            }
        }
    }

    @Composable
    fun PictureList(
        viewModel: PictureViewModel = providePictureViewModelFactory(applicationContext)
    ) {
        val listState by viewModel.uiState.collectAsState()
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = listState.list,
                itemContent = {
                    PictureDetailsItem(it, listState.searchTag, listState.list.indexOf(it))
                })
        }
    }

    @Composable
    fun PictureDetailsScreen() {
        Button(onClick = { navController.navigate("pictureList") }) {
            Text("Go back to list!")
        }
    }

    @Composable
    fun PictureDetailsItem(
        details: PictureDetails, searchTag: String, num: Int
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable(enabled = true, onClick = { navController.navigate("pictureDetails") }),
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Picture(details.url)
                Column ( modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)) {
                    Text(text = details.title, style = typography.h6)
                    Text(text = "Num: $num. Search tag: $searchTag", style = TextStyle(fontStyle = FontStyle.Italic))
                }
            }
        }

    }
