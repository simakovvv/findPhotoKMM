package com.electrolux.findphoto.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.electrolux.findphoto.Greeting
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electrolux.findphoto.PicturesSDK
import com.electrolux.findphoto.android.compose.ui.AppTheme
import com.electrolux.findphoto.cache.DatabaseDriverFactory
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()*/
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
        val navController = rememberNavController()
        NavHost(navController, startDestination = "pictureList") {
            composable(route = "pictureList") {
                PictureList(navController)
            }
            composable(route = "pictureDetails") {
                PictureDetailsScreen(navController)
            }
        }
    }

    @Composable
    fun PictureList(
        navController: NavController,
        viewModel: PictureViewModel = providePictureViewModelFactory(applicationContext)
    ) {
        val uiState by viewModel.uiState.collectAsState()
        viewModel.sendIntent(PictureListIntent.LoadList("Electrolux"))
        when(uiState) {
            is PictureListState.Error -> {}//TODO()
            PictureListState.Loading -> {}// TODO()
            is PictureListState.UpdateItem -> {}//TODO()
            is PictureListState.UpdateList -> {
                Log.d("Flickr search", (uiState as PictureListState.UpdateList).list.size.toString())
            }//TODO()
        }
        Button(onClick = { navController.navigate("pictureDetails") }) {
            Text("Recycler view will be here")
        }.apply {

        }
    }

    @Composable
    fun PictureDetailsScreen(navController: NavController) {
        Button(onClick = { navController.navigate("pictureList") }) {
            Text("Go back to list!")
        }
    }
}
