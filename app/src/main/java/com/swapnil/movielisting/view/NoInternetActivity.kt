package com.swapnil.movielisting.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swapnil.movielisting.R
import com.swapnil.movielisting.extensions.Spacer
import com.swapnil.movielisting.ui.theme.MovieListingTheme
import com.swapnil.movielisting.util.internet.ConnectionState
import com.swapnil.movielisting.util.internet.InternetStateChangeNotifier

class NoInternetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        InternetStateChangeNotifier.getInstance(this).observe(this) { connectionState ->
            when (connectionState) {
                ConnectionState.CONNECTED -> {
                    onInternetConnectionWorking()
                }

                ConnectionState.SLOW -> {
                    onInternetConnectionWorking()
                }

                else -> {

                }
            }
        }

        setContent {
            MovieListingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .navigationBarsPadding()
                        .systemBarsPadding()
                ) {
                    NoInternetScreen()
                }
            }
        }
    }


    @Composable
    private fun NoInternetScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(dp = 23.dp)
            Text(
                text = stringResource(id = R.string.no_internet).uppercase(),
            )
            Spacer(dp = 16.dp)
            Text(
                text = stringResource(id = R.string.try_wifi)
            )
        }
    }

    private fun onInternetConnectionWorking() {
        launchMainActivity()
        finish()
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
        finishAffinity()
    }
}