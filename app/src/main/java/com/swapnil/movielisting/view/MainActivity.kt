package com.swapnil.movielisting.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swapnil.movielisting.routing.Router
import com.swapnil.movielisting.ui.theme.MovieListingTheme
import com.swapnil.movielisting.util.internet.ConnectionState
import com.swapnil.movielisting.util.internet.InternetStateChangeNotifier
import com.swapnil.movielisting.view.feed.FeedNavigation
import com.swapnil.movielisting.view.preview.PreviewNavigation
import com.swapnil.movielisting.view.states.NoInternetActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initInternetState()
        setContent {
            MovieListingTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .systemBarsPadding()
                ) { innerPadding ->
                    MovieFeedNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun MovieFeedNavHost(modifier: Modifier) {
        val navController = rememberNavController()
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Router.MovieFeeds.key
        ) {
            composable(Router.MovieFeeds.key) {
                FeedNavigation(navController = navController)
            }

            composable(Router.MovieDetails.key) {
                PreviewNavigation(navBackStack = it, navController = navController)
            }
        }
    }



    private fun initInternetState() {
        InternetStateChangeNotifier.getInstance(this).observe(this) { connectionState ->
            when (connectionState) {
                ConnectionState.CONNECTED -> {
                    "Connected"
                }

                ConnectionState.SLOW -> {
                    "Slow Internet Connection"
                }

                else -> {
                    launchNoInternetActivity()
                }
            }
        }
    }

    private fun launchNoInternetActivity() {
        val noInternetIntent = Intent(this, NoInternetActivity::class.java)
        noInternetIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(noInternetIntent)
        finishAffinity()
    }
}