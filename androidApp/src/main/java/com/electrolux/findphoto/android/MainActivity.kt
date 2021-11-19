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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.request.CachePolicy
import com.electrolux.findphoto.android.compose.ui.PictureDetailsScreen
import com.electrolux.findphoto.android.compose.ui.PictureList

import com.electrolux.findphoto.android.viewModel.Injector.providePictureDetailsViewModelFactory
import com.electrolux.findphoto.android.viewModel.Injector.providePictureListViewModelFactory
import com.electrolux.findphoto.entity.PictureDetails

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
        NavHost(navController, startDestination = LIST_DESTINATION) {
            composable(route = LIST_DESTINATION) {
                PictureList(applicationContext, navController)
            }
            composable(
                route = "$DETAILS_DESTINATION/{$DETAILS_ITEM_ID}",
                arguments = listOf(navArgument(DETAILS_ITEM_ID) { type = NavType.IntType })
            ) { backStackEntry ->
                val pictureId = backStackEntry.arguments?.getInt(DETAILS_ITEM_ID)
                PictureDetailsScreen(applicationContext, navController, pictureId)
            }
        }
    }
}

