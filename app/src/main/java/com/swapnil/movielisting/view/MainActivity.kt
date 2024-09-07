package com.swapnil.movielisting.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.swapnil.movielisting.ui.theme.MovieListingTheme
import com.swapnil.movielisting.view.viewmodel.MovieListAction
import com.swapnil.movielisting.view.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieListingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val moviesViewModel: MovieListViewModel = hiltViewModel()
    val state by moviesViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        moviesViewModel.processAction(MovieListAction.OnLaunch)
    }
    when {
        state.isLoading -> {
            Text(
                text = "Loading...",
                modifier = modifier
            )
        }
        state.error != null -> {
            Text(
                text = "Error: ${state.error}",
                modifier = modifier
            )
        }
        else -> {
            Text(
                text = "Movies: ${state.movies}",
                modifier = modifier.fillMaxSize()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieListingTheme {
        Greeting("Android")
    }
}